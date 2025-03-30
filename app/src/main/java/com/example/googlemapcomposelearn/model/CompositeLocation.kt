package com.example.googlemapcomposelearn.model

import com.google.android.gms.maps.model.LatLng

data class CompositeLocation(
//    val latLng: LatLng = LatLng(0.0, 0.0),
    val latLng: LatLng? = null,
    val label: String? = null,
)
