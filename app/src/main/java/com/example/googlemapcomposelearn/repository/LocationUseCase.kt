package com.example.googlemapcomposelearn.repository

import android.Manifest
import android.content.Context
import android.content.IntentSender
import androidx.annotation.RequiresPermission
import com.example.googlemapcomposelearn.MainActivity
import com.example.googlemapcomposelearn.model.CompositeLocation
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

class LocationUseCase @Inject constructor(
    private val locationRepository: LocationRepository,
    scope: CoroutineScope
) {
    val currentLocation = locationRepository.latestLocation.mapNotNull { latLng ->
        println("xxxx log location UC $latLng")
        latLng?.let {
            CompositeLocation(
                latLng = it,
                label = "Current Location",
            )
        }
    }.shareIn(
        scope = scope,
        started = SharingStarted.WhileSubscribed(5.seconds.inWholeMilliseconds),
        replay = 1,
    )

    @RequiresPermission(allOf = [Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION])
    fun updateLocation() {
        locationRepository.updateLocation()
    }

    fun enableUserDeviceLocation(context: Context) {
        locationRepository.enableUserDeviceLocation(context)
    }
}