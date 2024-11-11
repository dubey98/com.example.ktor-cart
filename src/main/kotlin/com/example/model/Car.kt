package com.example.model

import javax.inject.Inject

class Car @Inject constructor() {

    fun drive() {
        println("driving...")
    }
}