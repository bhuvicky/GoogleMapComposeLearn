package com.example.googlemapcomposelearn.nearbysearch

import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.CircularBounds
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.Place.Field.ID
import com.google.android.libraries.places.api.model.Place.Field.LOCATION
import com.google.android.libraries.places.api.model.Place.Field.DISPLAY_NAME
import com.google.android.libraries.places.api.model.Place.Field.TYPES
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.api.net.SearchNearbyRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.Arrays
import javax.inject.Inject


class NearbySearchRepository@Inject constructor(private val placesClient: PlacesClient) {

    val placeFields: List<Place.Field> = Arrays.asList(
        ID, DISPLAY_NAME, LOCATION, TYPES
    )

    suspend operator fun invoke(
        centerPlace: LatLng,
        includeTypes: List<String>
    ): List<Place> {
        val circle = CircularBounds.newInstance(centerPlace,  /* radius = */4000.0)
        return withContext(Dispatchers.IO) {
            val request = SearchNearbyRequest.builder(circle, placeFields)
                .setIncludedTypes(includeTypes)
                .setMaxResultCount(10)
                .build()
            placesClient.searchNearby(request).await().places
        }
    }
}