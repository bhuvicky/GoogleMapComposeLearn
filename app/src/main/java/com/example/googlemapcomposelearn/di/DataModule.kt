package com.example.googlemapcomposelearn.di

import android.app.Application
import com.example.googlemapcomposelearn.GoogleMapComposeLearnApp
import com.example.googlemapcomposelearn.repository.LocationRepository
import com.example.googlemapcomposelearn.repository.LocationUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import kotlinx.coroutines.DelicateCoroutinesApi

@Module
@InstallIn(ViewModelComponent::class)
class DataModule {

    @OptIn(DelicateCoroutinesApi::class)
    @Provides
    fun provideLocationUseCase(flightRepository: LocationRepository,
                               application: Application
    ) = LocationUseCase(flightRepository,
        (application as GoogleMapComposeLearnApp).applicationScope)
}