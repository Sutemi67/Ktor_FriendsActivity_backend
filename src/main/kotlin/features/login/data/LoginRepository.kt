package apc.appcradle.features.login.data

import apc.appcradle.features.activity.data.Users
import apc.appcradle.features.login.model.LoginChangeRequest
import apc.appcradle.features.login.model.LoginRequest
import apc.appcradle.features.login.model.LoginResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import java.util.*

class LoginRepository() {

    suspend fun loginUser(call: ApplicationCall) {
        val receive = call.receive<LoginRequest>()
        val userDTO = Users.getUserData(receive.login)

        if (userDTO == null) {
            call.respond(HttpStatusCode.BadRequest, "User does not exists")
            println("User ${receive.login} does not exists")
        } else {
            if (userDTO.password == receive.password) {
                val token = UUID.randomUUID().toString()
                call.respond(LoginResponse(token = token))
                println("User ${receive.login} - successful login, got a new token")
            } else {
                call.respond(HttpStatusCode.BadRequest, "Password is incorrect")
                println("User ${receive.login} password incorrect")
            }
        }
    }

    suspend fun changeLogin(call: ApplicationCall) {
        val receive = call.receive<LoginChangeRequest>()
        val userSQL = Users.getUserData(receive.login)
        if (userSQL == null) {
            call.respond(HttpStatusCode.BadRequest, "User does not exists")
            println("User ${receive.login} does not exists")
        } else {
            val isSuccess = Users.changeLogin(userSQL.copy(changeLogin = receive.newLogin))
            if (isSuccess.isNullOrEmpty()) {
                call.respond(HttpStatusCode.OK, "login changed")
                println("User ${userSQL.login} changed login to ${receive.newLogin}")
            } else {
                call.respond(HttpStatusCode.BadRequest, "error due to nick changing")
                println("User ${userSQL.login} error in nick change")
            }
        }
    }
}