package apc.appcradle.features.login.router

import apc.appcradle.features.login.data.LoginRepository
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureLoginRouting() {
    val controller = LoginRepository()

    routing {
        post("/login") {
            controller.loginUser(call)
        }
        post("/login_update") {
            controller.changeLogin(call)
        }
    }
}