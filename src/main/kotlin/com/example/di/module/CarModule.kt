package com.example.di.module

import com.example.model.Car
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class CarModule {
    @Provides
    @Singleton
    fun provideCar(): Car {
        return Car()
    }
}