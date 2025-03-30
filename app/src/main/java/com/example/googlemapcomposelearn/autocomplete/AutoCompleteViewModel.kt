package com.example.googlemapcomposelearn.autocomplete

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.googlemapcomposelearn.R
import com.example.googlemapcomposelearn.model.AutoCompletePlace
import com.example.googlemapcomposelearn.model.MapUiEvent
import com.example.googlemapcomposelearn.nearbysearch.NearbyPlaceModel
import com.example.googlemapcomposelearn.nearbysearch.NearbySearchRepository
import com.example.googlemapcomposelearn.nearbysearch.PlaceCategory
import com.example.googlemapcomposelearn.repository.LocationUseCase
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class AutoCompleteViewModel @Inject constructor(
    private val getAutocompletionPredictionsUseCase: GetAutocompletionPredictionsUseCase,
    private val placesRepository: PlaceRepository,
    private val locationUseCase: LocationUseCase,
    private val nearbySearchRepository: NearbySearchRepository,

): ViewModel() {
    private val autocompleteDebounce = 500.milliseconds

    private val _searchText = MutableStateFlow("")
    private val searchText: StateFlow<String> = _searchText.asStateFlow()

    private val _selectedPlace: MutableStateFlow<AutoCompletePlace?> = MutableStateFlow(null)
    private val selectedPlace: StateFlow<AutoCompletePlace?> = _selectedPlace.asStateFlow()

    private val _nearbyPlaces = MutableStateFlow<List<NearbyPlaceModel>?>(null)
    val nearbyPlaces: StateFlow<List<NearbyPlaceModel>?> = _nearbyPlaces.asStateFlow()

    private val actions = MutableStateFlow<FindAutocompletePredictionsRequest.Builder.() -> Unit> {}
    private val query = searchText.combine(actions) { text, actions ->
        AutoCompleteQuery(text, actions)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private val predictions = query
        .debounce(autocompleteDebounce)
        .mapLatest { query ->
            if (query.query.isNotBlank()) {
                getAutocompletionPredictionsUseCase(query.query, query.actions)
            } else {
                emptyList()
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5.seconds), emptyList())

    private var autocompletePlaces = predictions.map { predictions ->
        predictions.map {
            println("xxxx log predictions types : ${it.types}")
        }
        predictions.map(AutocompletePrediction::toPlaceDetails)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5.seconds), emptyList())

    @OptIn(ExperimentalCoroutinesApi::class)
    val selectedPlaceWithLocation = selectedPlace.mapLatest { selectedPlace ->
        selectedPlace?.let { place ->
            placesRepository.getPlaceLatLng(place.placeId).second.location?.let { latLng ->
                place.copy(latLng = latLng)
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5.seconds), null)

    val autocompleteViewState = combine(
        autocompletePlaces,
        searchText,
        selectedPlaceWithLocation,
    ) { autocompletePlaces, searchText, selectedPlaceWithLocation ->
        AutocompleteViewState(
            predictions = autocompletePlaces,
            searchText = searchText,
            selectedPlace = selectedPlaceWithLocation,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5.seconds),
        initialValue = AutocompleteViewState()
    )

    fun onMapUiEvent(event: MapUiEvent) {
        when (event) {
            is MapUiEvent.OnPlaceSelected -> {
                _selectedPlace.value = event.selectedPlace
            }
            is MapUiEvent.OnQueryTextChanged -> {
                _searchText.value = event.query
                this.actions.value = event.actions
                _selectedPlace.value = null
                _nearbyPlaces.value = null
            }
            is MapUiEvent.OnPlaceCategorySelected -> {
                _searchText.value = ""
                _selectedPlace.value = null
                getNearbyPlacesWithLatLng(event)
            }
        }
    }

    private fun getNearbyPlacesWithLatLng(event: MapUiEvent.OnPlaceCategorySelected) {
        println("xxxx log getNearbyPlacesWithLatLng")
        _nearbyPlaces.value = null
        viewModelScope.launch {
            val compositeLocation = locationUseCase.currentLocation.firstOrNull()
            println("xxxx log CL = ${compositeLocation?.latLng}; category = ${event.placeCategory.categoryName}")
            val nearbyPlaces = nearbySearchRepository(compositeLocation?.latLng!!, listOf(event.placeCategory.categoryName))
            val nearbyPlacesWithLatLng = nearbyPlaces.map { nearbyPlace ->
                viewModelScope.async { placesRepository.getPlaceLatLng(nearbyPlace.id.orEmpty()).second}
            }.awaitAll()
            _nearbyPlaces.value = getNearbyPlaces(nearbyPlacesWithLatLng, event.placeCategory)
        }
    }

    private fun getNearbyPlaces(
        locations: List<Place>,
        placeCategory: PlaceCategory
    ): List<NearbyPlaceModel> {
        val categoryIcon = when (placeCategory) {
            PlaceCategory.RESTAURANT -> R.drawable.icons8_restaurant_on_site_24
            PlaceCategory.GAS_STATION -> R.drawable.icons8_gas_station_24
            PlaceCategory.SHOPPING_MALL -> R.drawable.icons8_shopping_mall_24
        }
        return locations.map {
            NearbyPlaceModel(it.location!!, it.displayName.orEmpty(), categoryIcon)
        }
    }
}