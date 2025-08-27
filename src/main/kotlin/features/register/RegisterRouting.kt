package apc.appcradle.features.register

import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRegisterRouting() {
    val controller = RegisterController()

    routing {
        post("/register") {
            controller.registerUser(call)
        }
    }
}