package apc.appcradle.features.register.data

import apc.appcradle.features.activity.data.Users
import apc.appcradle.features.activity.model.UserSQL
import apc.appcradle.features.login.data.Tokens
import apc.appcradle.features.login.model.TokenDTO
import apc.appcradle.features.register.model.RegisterRequest
import apc.appcradle.features.register.model.RegisterResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import java.util.*

class RegisterRepository() {

    suspend fun registerUser(call: ApplicationCall) {
        try {
            val request = call.receive<RegisterRequest>()
            val userSQL = Users.getUserData(request.login)

            if (userSQL != null) {
                call.respond(HttpStatusCode.Conflict, "User is already exists")
                println("Existing user try: ${userSQL.login}")
                return
            }
            val token = UUID.randomUUID().toString()
            Users.insert(
                userSQL = UserSQL(
                    login = request.login,
                    password = request.password,
                    steps = 0,
                    weeklySteps = 0
                )
            )
            Tokens.insert(
                tokenDTO = TokenDTO(
                    login = request.login,
                    token = token
                )
            )
            call.respond(RegisterResponse(token = token))
            println("Successful register happened: ${request.login}")
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, "Error in register action :(")
            println(e.message)
        }
    }
}