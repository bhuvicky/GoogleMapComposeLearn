package com.example.googlemapcomposelearn.di

import android.app.Activity
import android.app.Application
import android.content.Context
import com.example.googlemapcomposelearn.GoogleMapComposeLearnApp
import com.example.googlemapcomposelearn.autocomplete.ApiKeyProvider
import com.example.googlemapcomposelearn.autocomplete.AutocompleteRepository
import com.example.googlemapcomposelearn.nearbysearch.NearbySearchRepository
import com.example.googlemapcomposelearn.repository.LocationRepository
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @OptIn(DelicateCoroutinesApi::class)
    @Provides
    @Singleton
    fun provideLocationRepository(
        @ApplicationContext application: Context): LocationRepository {
        return LocationRepository(
            application.applicationContext,
            (application as GoogleMapComposeLearnApp).applicationScope
        )
    }

    @Provides
    @Singleton
    fun providePlacesClient(
        application: Application,
        apiKeyProvider: ApiKeyProvider
    ): PlacesClient {
        Places.initializeWithNewPlacesApiEnabled(
            application.applicationContext,
            apiKeyProvider.mapsApiKey
        )
        return Places.createClient(application.applicationContext)
    }

    @Provides
    @Singleton
    fun provideAutocompleteRepository(placesClient: PlacesClient): AutocompleteRepository {
        return AutocompleteRepository(placesClient)
    }

    @Provides
    @Singleton
    fun provideApiKeyProvider(application: Application): ApiKeyProvider {
        return ApiKeyProvider(application.applicationContext)
    }

    @Provides
    @Singleton
    fun provideNearbySearchRepository(placesClient: PlacesClient): NearbySearchRepository {
        return NearbySearchRepository(placesClient)
    }
}