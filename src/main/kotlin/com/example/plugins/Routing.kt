package com.example.plugins

import com.example.model.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

val cart:Cart = Cart()

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

        put("/cart/update/{itemId}") {
            call.respondText("update an item in cart")
        }

        delete("/cart/remove/{itemId}") {
            call.respondText("delete an item from cart")
        }
    }
}
