package apc.appcradle.features.login

import apc.appcradle.database.users.Users
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import java.util.*

class LoginController() {

    suspend fun loginUser(call: ApplicationCall) {
        val receive = call.receive<LoginReceiveRemote>()
        val userDTO = Users.fetchUser(receive.login)

        if (userDTO == null) {
            call.respond(HttpStatusCode.BadRequest, "User does not exists")
            println("User ${receive.login} does not exists")
        } else {
            if (userDTO.password == receive.password) {
                val token = UUID.randomUUID().toString()
                call.respond(LoginResponseRemote(token = token))
                println("User ${receive.login} - successful login, got a new token")
            } else {
                call.respond(HttpStatusCode.BadRequest, "Password is incorrect")
                println("User ${receive.login} password incorrect")
            }
        }
    }

    suspend fun changeLogin(call: ApplicationCall) {
        val receive = call.receive<LoginChange>()
        val userDTO = Users.fetchUser(receive.login)
        if (userDTO == null) {
            call.respond(HttpStatusCode.BadRequest, "User does not exists")
            println("User ${receive.login} does not exists")
        } else {
            val isSuccess = Users.changeLogin(userDTO.copy(changeLogin = receive.newLogin))
            if (isSuccess.isNullOrEmpty()) {
                call.respond(HttpStatusCode.OK, "login changed")
                println("User ${userDTO.login} changed login to ${receive.newLogin}")
            } else {
                call.respond(HttpStatusCode.BadRequest, "error due to nick changing")
                println("User ${userDTO.login} error in nick change")
            }
        }
    }
}