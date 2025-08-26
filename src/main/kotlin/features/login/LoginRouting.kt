package apc.appcradle.features.login

import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureLoginRouting() {
    routing {
        post("/login") {
            val controller = LoginController(call)
            controller.loginUser()
        }
    }
}
