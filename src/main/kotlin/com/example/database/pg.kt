package com.example.database

import com.example.model.CartItems
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {
    fun init() {
        val url = System.getenv("DATABASE_URL") ?: "jdbc:postgresql://postgres:5432/postgres"
        val driver = "org.postgresql.Driver"
        val user = System.getenv("DATABASE_USER") ?: "postgres"
        val password = System.getenv("DATABASE_PASSWORD") ?: "example"

        println("$url $driver $user $password")

        Database.connect(url = url, driver = driver, user = user, password = password)


        transaction {
            SchemaUtils.create(CartItems)
        }
    }
}