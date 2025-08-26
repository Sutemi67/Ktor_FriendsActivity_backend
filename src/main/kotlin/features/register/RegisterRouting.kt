package apc.appcradle.features.register

import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRegisterRouting() {

    routing {
        post("/register") {
            val controller = RegisterController(call)
            controller.registerUser()
        }
    }
}