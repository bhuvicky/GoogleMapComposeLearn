package com.example.googlemapcomposelearn.autocomplete

import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AutocompleteRepository @Inject constructor(private val placesClient: PlacesClient) {

    suspend fun getAutocompletePlaces(request: FindAutocompletePredictionsRequest) : List<AutocompletePrediction> {
        return withContext(Dispatchers.IO) {
            placesClient.findAutocompletePredictions(request).await().autocompletePredictions
        }
    }
}
