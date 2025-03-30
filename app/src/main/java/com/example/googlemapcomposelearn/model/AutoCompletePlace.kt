package com.example.googlemapcomposelearn.model

import android.text.Spannable
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.Place

data class AutoCompletePlace(
    val placeId: String,
    val primaryText: Spannable,
    val placeType: List<Place.Type>? = null,
    val latLng: LatLng? = null
)
