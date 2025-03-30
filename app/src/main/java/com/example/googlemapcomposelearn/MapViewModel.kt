package com.example.googlemapcomposelearn

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.googlemapcomposelearn.autocomplete.PlaceRepository
import com.example.googlemapcomposelearn.model.CompositeLocation
import com.example.googlemapcomposelearn.model.MapUiEvent
import com.example.googlemapcomposelearn.nearbysearch.NearbyPlaceModel
import com.example.googlemapcomposelearn.nearbysearch.NearbySearchRepository
import com.example.googlemapcomposelearn.nearbysearch.PlaceCategory
import com.example.googlemapcomposelearn.nearbysearch.PlaceCategoryChipModel
import com.example.googlemapcomposelearn.repository.LocationUseCase
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.Place
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.selects.whileSelect
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class MapViewModel @Inject constructor(
    private val locationUseCase: LocationUseCase,
    private val nearbySearchRepository: NearbySearchRepository,
    private val placesRepository: PlaceRepository
): ViewModel() {

    private val _placeCategoryList = MutableStateFlow(emptyList<PlaceCategoryChipModel>())
    val placeCategoryList: StateFlow<List<PlaceCategoryChipModel>> = _placeCategoryList.asStateFlow()

    private val _nearbyPlaces = MutableStateFlow<List<NearbyPlaceModel>?>(null)
    val nearbyPlaces: StateFlow<List<NearbyPlaceModel>?> = _nearbyPlaces.asStateFlow()

    val location = locationUseCase.currentLocation.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5.seconds.inWholeMilliseconds),
        initialValue = CompositeLocation()
    )

    init {
        getPlaceCategoryData()
    }

    @SuppressLint("MissingPermission")
    fun getCurrentLocation() {
        locationUseCase.updateLocation()
    }

    fun enableUserDeviceLocation(context: Context) {
        locationUseCase.enableUserDeviceLocation(context)
    }

    fun onMapUiEvent(event: MapUiEvent) {
        when (event) {
            is MapUiEvent.OnPlaceCategorySelected -> {

            } else -> {}
        }
    }

    private fun getPlaceCategoryData() = _placeCategoryList.update {
        listOf(
            PlaceCategoryChipModel(PlaceCategory.RESTAURANT, R.drawable.icons8_restaurant_on_site_24),
            PlaceCategoryChipModel(PlaceCategory.GAS_STATION, R.drawable.icons8_gas_station_24),
            PlaceCategoryChipModel(PlaceCategory.SHOPPING_MALL, R.drawable.icons8_shopping_mall_24),
        )
    }
}