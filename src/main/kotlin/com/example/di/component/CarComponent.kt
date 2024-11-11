package com.example.di.component

import com.example.model.Car
import dagger.Component

@Component
interface CarComponent {
    fun getCar(): Car
}