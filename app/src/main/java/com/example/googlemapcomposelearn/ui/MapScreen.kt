package com.example.googlemapcomposelearn.ui

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.googlemapcomposelearn.MapViewModel
import com.example.googlemapcomposelearn.R
import com.example.googlemapcomposelearn.autocomplete.AutoCompleteViewModel
import com.example.googlemapcomposelearn.autocomplete.AutocompleteViewState
import com.example.googlemapcomposelearn.model.AutoCompletePlace
import com.example.googlemapcomposelearn.model.CompositeLocation
import com.example.googlemapcomposelearn.model.MapUiEvent
import com.example.googlemapcomposelearn.nearbysearch.NearbyPlaceModel
import com.example.googlemapcomposelearn.nearbysearch.PlaceCategoryChipModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.CircularBounds
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MapScreen() {
    val viewModel = hiltViewModel<MapViewModel>()
    val autoCompleteViewModel = hiltViewModel<AutoCompleteViewModel>()
    val context = LocalContext.current
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(51.5074, -0.1278), 10f)
    }
    val locationPermissionsState = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
        )
    )
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            viewModel.getCurrentLocation()
        } else {
            println("Location permission was denied by the user.")
        }
    }

    val currentLocation by viewModel.location.collectAsStateWithLifecycle()
    val autoCompleteViewState by autoCompleteViewModel.autocompleteViewState.collectAsStateWithLifecycle()
    val selectedPlace by autoCompleteViewModel.selectedPlaceWithLocation.collectAsStateWithLifecycle()
    val placeCategoryList by viewModel.placeCategoryList.collectAsStateWithLifecycle()
    val nearbyPlaces by autoCompleteViewModel.nearbyPlaces.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.enableUserDeviceLocation(context)
        checkLocationPermission(
            locationPermissionsState,
            permissionLauncher,
            context,
            viewModel::getCurrentLocation
        )
    }

    MapScreen(
        cameraPositionState,
        currentLocation,
        autoCompleteViewState,
        selectedPlace,
//        viewModel::onMapUiEvent,
        autoCompleteViewModel::onMapUiEvent,
        placeCategoryList,
        nearbyPlaces
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MapScreen(
    cameraPositionState: CameraPositionState,
    currentLocation: CompositeLocation,
    autocompleteViewState: AutocompleteViewState,
    selectedPlace: AutoCompletePlace?,
//    onMapUiEvent: (MapUiEvent) -> Unit,
    onAutoCompleteUiEvent: (MapUiEvent) -> Unit,
    placeCategoryList: List<PlaceCategoryChipModel>,
    nearbyPlaces: List<NearbyPlaceModel>?,
) {
    Scaffold { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
           DisplayGoogleMap(
                cameraPositionState = cameraPositionState,
                userLocation = currentLocation,
               selectedPlace = selectedPlace,
               nearbyPlaces = nearbyPlaces
           )
            Column(
                modifier = Modifier.fillMaxWidth().wrapContentHeight().align(Alignment.TopCenter)
            ) {
                AutoCompleteSearchBar(
                    userLocation = currentLocation,
                    autocompleteViewState = autocompleteViewState,
                    selectedPlace = selectedPlace,
//                    onMapUiEvent = onMapUiEvent,
                    onAutoCompleteUiEvent = onAutoCompleteUiEvent,
                    placeCategoryList = placeCategoryList
                )
            }
       }
    }
}

@Composable
private fun DisplayGoogleMap(
    cameraPositionState: CameraPositionState,
    userLocation: CompositeLocation,
    selectedPlace: AutoCompletePlace?,
    nearbyPlaces: List<NearbyPlaceModel>? = null,

) {
    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState
    ){
        userLocation.latLng?.let {
            Marker(
                state = rememberMarkerState(position = it),
                title = userLocation.label.orEmpty(),
                icon = BitmapDescriptorFactory.fromResource(R.drawable.icons8_filled_circle_24),
                snippet = "This is where you are currently located."
            )
//                cameraPositionState.animate(CameraUpdateFactory.newLatLng(it), 1000)
            cameraPositionState.position = CameraPosition.fromLatLngZoom(it, 10f)
        }

        selectedPlace?.let {
            Marker(
                state = rememberMarkerState(position = it.latLng!!),
                title = it.primaryText.toString(),
                icon = BitmapDescriptorFactory.fromResource(R.drawable.icons8_location_24),
            )
            cameraPositionState.position = CameraPosition.fromLatLngZoom(it.latLng, 13f)
        }

        nearbyPlaces?.forEach { nearbyPlace ->
            Marker(
                state = rememberMarkerState(position = nearbyPlace.location),
                title = nearbyPlace.title,
                icon = BitmapDescriptorFactory.fromResource(nearbyPlace.icon),
            )
            cameraPositionState.position = CameraPosition.fromLatLngZoom(nearbyPlace.location, 13f)
        }
    }
}

@Composable
fun AutoCompleteSearchBar(
    userLocation: CompositeLocation,
    autocompleteViewState: AutocompleteViewState,
    selectedPlace: AutoCompletePlace?,
//    onMapUiEvent: (MapUiEvent) -> Unit,
    onAutoCompleteUiEvent: (MapUiEvent) -> Unit,
    placeCategoryList: List<PlaceCategoryChipModel>,
) {
    PlacesAutocompleteTextFieldV2(
//                modifier = Modifier.padding(16.dp),
        searchText = autocompleteViewState.searchText,
        predictions = selectedPlace?.let{ emptyList() } ?: autocompleteViewState.predictions,
        onQueryChanged = { query ->
            onAutoCompleteUiEvent(MapUiEvent.OnQueryTextChanged(query = query,
                actions = {
                    origin = userLocation.latLng
                    locationRestriction = userLocation.latLng?.let { CircularBounds.newInstance(it, 10000.0) }
                }
            ))
        },
        onPlaceSelected = { place ->
            onAutoCompleteUiEvent(MapUiEvent.OnPlaceSelected(place))
        }
    )
    PlaceChipGroupLayout(
        placeCategoryList = placeCategoryList,
        onPlaceCategorySelected = onAutoCompleteUiEvent
    )
}

@OptIn(ExperimentalPermissionsApi::class)
//@Composable
private fun checkLocationPermission(locationPermissionsState: MultiplePermissionsState,
                                    permissionLauncher: ManagedActivityResultLauncher<String, Boolean>,
                                    context: Context,
                                    getCurrentLocation: () -> Unit) {
   /* if (locationPermissionsState.allPermissionsGranted) {
        println("xxxxx log permission granted")
        getCurrentLocation()
    } else {
        locationPermissionsState.launchMultiplePermissionRequest()
    }*/
    when (PackageManager.PERMISSION_GRANTED) {
        ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) -> {
            getCurrentLocation()
        }
        else -> {
            permissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }
}
