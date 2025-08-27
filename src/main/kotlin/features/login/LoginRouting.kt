package apc.appcradle.features.login

import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureLoginRouting() {
    val controller = LoginController()

    routing {
        post("/login") {
            controller.loginUser(call)
        }
        post("/login_update") {
            controller.changeLogin(call)
        }
    }
}