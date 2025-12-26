package apc.appcradle.features.register.router

import apc.appcradle.features.register.data.RegisterRepository
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRegisterRouting() {
    val controller = RegisterRepository()

    routing {
        post("/register") {
            controller.registerUser(call)
        }
    }
}