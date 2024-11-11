package com.example

import com.example.database.DatabaseFactory
import com.example.di.component.DaggerCartServiceComponent
import com.example.plugins.*
import io.ktor.server.application.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.plugins.contentnegotiation.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    DatabaseFactory.init()

    install(ContentNegotiation) {
        json()
    }

    val cartService = DaggerCartServiceComponent.create().getCartService()

    configureRouting(cartService)
}