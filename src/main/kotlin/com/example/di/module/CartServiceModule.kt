package com.example.di.module

import com.example.services.CartService
import com.example.services.CartServiceImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class CartServiceModule {
    @Provides
    fun provideCartService(): CartService{
        return CartServiceImpl()
    }
}