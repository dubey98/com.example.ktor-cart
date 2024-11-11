package com.example

import com.example.database.DatabaseFactory
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

    configureRouting()
}

//class KtorApplication: Application() {
//    @Inject
//    late init var cartService: CartService
//
//    fun Application.module() {
//        val appComponent = AppComponent.create()
//        appComponent.inject(this@KtorApplication)
//
//        DatabaseFactory.init()
//
//        install(ContentNegotiation) {
//            json()
//        }
//
//        configureRouting(cartService)
//    }
//}