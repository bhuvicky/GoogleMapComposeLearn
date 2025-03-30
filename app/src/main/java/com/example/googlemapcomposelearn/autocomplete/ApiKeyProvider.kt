package com.example.googlemapcomposelearn.autocomplete

import android.content.Context
import android.content.pm.PackageManager
import javax.inject.Inject

class ApiKeyProvider @Inject constructor(private val context: Context) {
    val mapsApiKey: String by lazy {
        getMapsApiKeyFromManifest()
    }

    private fun getMapsApiKeyFromManifest(): String {
        val mapsApiKey =
            try {
                val applicationInfo =
                    context.packageManager.getApplicationInfo(
                        context.packageName,
                        PackageManager.GET_META_DATA,
                    )
                applicationInfo.metaData?.getString("com.google.android.geo.API_KEY") ?: ""
            } catch (e: PackageManager.NameNotFoundException) {
                ""
            }
        if (mapsApiKey.isBlank()) {
            // TODO: get the right error message/behavior.
            error("MapsApiKey missing from AndroidManifest.")
        }
        return mapsApiKey
    }
}