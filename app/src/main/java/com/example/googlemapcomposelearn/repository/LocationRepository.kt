package com.example.googlemapcomposelearn.repository

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.IntentSender
import android.location.Location
import android.os.Looper
import androidx.annotation.RequiresPermission
import com.example.googlemapcomposelearn.MainActivity
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.CurrentLocationRequest
import com.google.android.gms.location.Granularity.GRANULARITY_FINE
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.location.Priority
import com.google.android.gms.location.Priority.PRIORITY_BALANCED_POWER_ACCURACY
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.Task
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

class LocationRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val scope: CoroutineScope
) {
    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    private val settingsClient  = LocationServices.getSettingsClient(context)

    private var locationRequest = with(LocationRequest.Builder(/* intervalMillis = */ 10.seconds.inWholeMilliseconds)) {
        setPriority(Priority.PRIORITY_HIGH_ACCURACY)
        setWaitForAccurateLocation(true)
        build()
    }

    private val _latestLocation = MutableSharedFlow<LatLng?>(1)
    val latestLocation: SharedFlow<LatLng?> = _latestLocation.asSharedFlow()

    @RequiresPermission(allOf = [ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION])
    fun updateLocation() {
        scope.launch {
//            val location = getLastLocation()
            val location = LatLng(12.898499498343485, 80.19988529999999)
            println("xxxxx log getting loc after delay $location")
            _latestLocation.tryEmit(location)
        }
    }

    fun enableUserDeviceLocation(context: Context) {
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
        val task: Task<LocationSettingsResponse> = settingsClient.checkLocationSettings(builder.build())
        task.addOnSuccessListener {
        }
        task.addOnFailureListener { exception ->
            if (exception is ResolvableApiException){
                // Location settings are not satisfied, but this can be fixed
                // by showing the user a dialog.
                try {
                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().
                    exception.startResolutionForResult(context as MainActivity,
                        100)
                } catch (sendEx: IntentSender.SendIntentException) {
                    // Ignore the error.
                }
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @RequiresPermission(allOf = [ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION])
    suspend fun getLastLocation(): LatLng? {
        val cancellationTokenSource = CancellationTokenSource()

        val result = fusedLocationClient.getCurrentLocation(
            CurrentLocationRequest.Builder()
                .setPriority(PRIORITY_BALANCED_POWER_ACCURACY)
                .setDurationMillis(5.seconds.inWholeMilliseconds)
                .setMaxUpdateAgeMillis(1.minutes.inWholeMilliseconds)
                .setGranularity(GRANULARITY_FINE)
                .build(),
            cancellationTokenSource.token,
        ).await(cancellationTokenSource)

        return result?.let { LatLng(it.latitude, it.longitude) }
    }
}