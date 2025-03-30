package com.example.googlemapcomposelearn

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope

@HiltAndroidApp
class GoogleMapComposeLearnApp: Application() {
    @OptIn(DelicateCoroutinesApi::class)
    val applicationScope = GlobalScope
}