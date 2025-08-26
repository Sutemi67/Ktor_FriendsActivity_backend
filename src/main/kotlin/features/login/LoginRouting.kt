package apc.appcradle.features.login

import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureLoginRouting() {
    routing {
        val controller = LoginController()
        post("/login") {
            controller.loginUser(call)
        }
        post("/login_update") {
            controller.changeLogin(call)
        }
    }
}
