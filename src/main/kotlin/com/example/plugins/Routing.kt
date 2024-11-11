package com.example.plugins

import com.example.model.*
import com.example.services.CartService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable

@Serializable
data class UpdateRequest (
    val quantity: Int? = null,
    val price: Double? = null,
    val name: String? = null
)

fun Application.configureRouting(cartService: CartService) {
    routing {
        get("/cart") {
            val cartItems = cartService.getAllItems()
            call.respond(cartItems)
        }

        post("/cart/add") {
            val cartItem = call.receive<CartItem>()

            val newItemId = cartService.addItemsInCart(cartItem)

            call.respond(HttpStatusCode.Created, "Item created with id $newItemId")
        }

        patch("/cart/update/{id}") {
            val itemId = call.parameters["id"]?.toIntOrNull()
            if (itemId == null) {
                call.respond(HttpStatusCode.BadRequest, "Missing ItemId Parameter")
                return@patch
            }

            try {
                val dataToUpdate = call.receive<UpdateRequest>()

                val updatedRows = cartService.updateItemInCart(itemId, dataToUpdate)

                if (updatedRows > 0) {
                    call.respond(HttpStatusCode.OK, "Successfully Updated")
                } else {
                    call.respond(HttpStatusCode.NotFound, "This id does not exist")
                }

            } catch (ex: IllegalArgumentException) {
                println(ex)
                call.respond(HttpStatusCode.BadRequest)
            }
        }

        delete("/cart/remove/{id}") {
            val itemId = call.parameters["id"]?.toIntOrNull()
            if (itemId == null) {
                call.respond(HttpStatusCode.BadRequest, "Missing id to delete item")
                return@delete
            }

            try {
                val deletedRows = cartService.removeItemFromCart(itemId)

                if (deletedRows > 0) {
                    call.respond(HttpStatusCode.OK, "Cart item deleted with id: $itemId")
                } else {
                    call.respond(HttpStatusCode.NotFound)
                }
            } catch (ex: IllegalArgumentException) {
                call.respond(HttpStatusCode.BadRequest)
            }
        }
    }
}
