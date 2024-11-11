package com.example.di.component

import com.example.di.module.CartServiceModule
import com.example.services.CartService
import dagger.Component

@Component(modules = [CartServiceModule::class])
interface CartServiceComponent {
    fun getCartService(): CartService
}