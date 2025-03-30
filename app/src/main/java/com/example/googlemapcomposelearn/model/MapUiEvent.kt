package com.example.googlemapcomposelearn.model

import com.example.googlemapcomposelearn.nearbysearch.PlaceCategory
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest

sealed class MapUiEvent() {
    data class OnPlaceCategorySelected(val placeCategory: PlaceCategory) : MapUiEvent()
    data class OnQueryTextChanged(val query: String, val actions: FindAutocompletePredictionsRequest.Builder.()->Unit) : MapUiEvent()
    data class OnPlaceSelected(val selectedPlace: AutoCompletePlace) : MapUiEvent()
}
