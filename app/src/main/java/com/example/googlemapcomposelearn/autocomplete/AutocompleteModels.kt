package com.example.googlemapcomposelearn.autocomplete

import android.text.Spannable
import com.example.googlemapcomposelearn.model.AutoCompletePlace
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest

data class AutocompleteViewState(
    val predictions: List<AutoCompletePlace> = emptyList(),
    val searchText: String = "",
    val selectedPlace: AutoCompletePlace? = null,
)

data class AutoCompleteQuery(
    val query: String,
    val actions: FindAutocompletePredictionsRequest.Builder.() -> Unit = {}
)

fun AutocompletePrediction.toPlaceDetails() =
    AutoCompletePlace(
        placeId = placeId,
        primaryText = getPrimaryText(predictionStyleSpan),
        placeType = placeTypes,
//        secondaryText = getSecondaryText(predictionStyleSpan),
//        distance = distanceMeters?.meters,
    )

/** StyleSpan applied by the to the [AutocompletePrediction]s to highlight the matches. */
private val predictionStyleSpan = android.text.style.StyleSpan(android.graphics.Typeface.BOLD)