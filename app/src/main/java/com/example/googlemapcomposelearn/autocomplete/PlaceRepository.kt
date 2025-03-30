package com.example.googlemapcomposelearn.autocomplete

import androidx.compose.runtime.mutableStateMapOf
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.Place.Field.DISPLAY_NAME
import com.google.android.libraries.places.api.model.Place.Field.ID
import com.google.android.libraries.places.api.model.Place.Field.LOCATION
import com.google.android.libraries.places.api.model.Place.Field.TYPES
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.api.net.kotlin.awaitFetchPlace
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Arrays
import javax.inject.Inject

class PlaceRepository @Inject constructor(
    private val placesClient: PlacesClient,
) {
    private val placesIdsWithLocations = mutableStateMapOf<String, Place>()
    private val placeFields: List<Place.Field> = Arrays.asList(
        ID, DISPLAY_NAME, LOCATION, TYPES
    )

    suspend fun getPlaceLatLng(placeId: String): Pair<String, Place> {
        return withContext(Dispatchers.Main) {
            placeId to placesIdsWithLocations.getOrElse(placeId) {
                withContext(Dispatchers.IO) {
                    placesClient.awaitFetchPlace(
                        placeId = placeId,
                        placeFields = placeFields
                    ).place.also { place ->
                        withContext(Dispatchers.Main) {
                            placesIdsWithLocations[placeId] = place
                        }
                    }
                }
            }
        }
    }
}