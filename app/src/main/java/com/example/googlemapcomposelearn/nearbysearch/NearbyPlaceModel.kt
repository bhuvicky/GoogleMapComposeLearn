package com.example.googlemapcomposelearn.nearbysearch

import androidx.annotation.DrawableRes
import com.google.android.gms.maps.model.LatLng

data class NearbyPlaceModel(
    val location: LatLng,
    val title: String,
    @DrawableRes val icon: Int
)
