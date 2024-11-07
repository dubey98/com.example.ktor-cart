package com.example.plugins

import com.example.model.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable

val cart:Cart = Cart()

@Serializable
data class UpdateRequest (
    val quantity: Int?  = null,
    val price:Double? = null,
    val name:String? = null
)

fun Application.configureRouting() {
    routing {
        get("/cart") {
            call.respond(cart.items)
        }

        post("/cart/add") {
            val cartItem = call.receive<CartItem>()
//            val isItemInCart = cart.items.contains(cartItem)
            val isItemInCart = cart.items.find({ item:CartItem -> item.itemId == cartItem.itemId}) != null

//            if(isItemInCart){
//                val itemInCart =  cart.items.find({item:CartItem -> item.itemId == cartItem.itemId})
//                itemInCart.quantity += 1
//            }else{
//                cart.items.add(cartItem)
//            }
            if(isItemInCart) {
                call.respond(HttpStatusCode.BadRequest, "Item already exists in cart")
            } else{
                cart.items.add(cartItem)
                call.respond(HttpStatusCode.Created, "Item SuccessFully Created!")
            }
        }

        patch("/cart/update/{itemId}") {
            val itemIdAsText = call.parameters["itemId"]
            if (itemIdAsText == null){
                call.respond(HttpStatusCode.BadRequest, "Missing ItemId Parameter")
            }

            try {
                val itemId = itemIdAsText?.toInt()
                val itemInCart = cart.items.find { item:CartItem -> item.itemId == itemId }

                if(itemInCart == null) {
                    call.respond(HttpStatusCode.NotFound)
                }

                val dataToUpdate = call.receive<UpdateRequest>()

                dataToUpdate.quantity?.let { itemInCart?.quantity = it as Int }

                dataToUpdate.price?.let { itemInCart?.price  = it as Double }

                call.respond(HttpStatusCode.OK, "Successfully Updated")
            } catch(ex: IllegalArgumentException){
                println(ex)
                call.respond(HttpStatusCode.BadRequest)
            }
        }

        delete("/cart/remove/{itemId}") {
            val itemIdAsText = call.parameters["itemId"]
            if(itemIdAsText == null) {
                call.respond(HttpStatusCode.BadRequest, "Missing Item id to delete item")
            }

            try {
                val itemId = itemIdAsText?.toInt()
                val cartItem = cart.items.find({ item: CartItem -> item.itemId == itemId})
                println("itemId $itemId")
                println(cartItem)

                if(cartItem == null){
                    call.respond(HttpStatusCode.NotFound)
                }

                cart.items.remove(cartItem)

                call.respond(HttpStatusCode.OK, "Item Successfully Deleted")
            } catch(ex: IllegalArgumentException) {
                call.respond(HttpStatusCode.BadRequest)
            }
        }
    }
}
