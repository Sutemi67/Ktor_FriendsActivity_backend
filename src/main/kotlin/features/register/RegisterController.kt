package apc.appcradle.features.register

import apc.appcradle.database.tokens.TokenDTO
import apc.appcradle.database.tokens.Tokens
import apc.appcradle.database.users.UserDTO
import apc.appcradle.database.users.Users
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import java.util.*

class RegisterController(private val call: ApplicationCall) {

    suspend fun registerUser() {
        try {
            val registerReceiveRemote = call.receive<RegisterReceiveRemote>()
            println("get post -> $registerReceiveRemote")

            val userDTO = Users.fetchUser(registerReceiveRemote.login)
            println("fetched userDTO -> ${userDTO?.login}, ${userDTO?.password}")

            if (userDTO != null) {
                call.respond(HttpStatusCode.Conflict, "User is already exists")
                println("Existing user try: ${userDTO.login}")
                return
            }
            val token = UUID.randomUUID().toString()

            Users.insert(
                userDTO = UserDTO(
                    login = registerReceiveRemote.login,
                    password = registerReceiveRemote.password,
                    steps = 0
                )
            )
            Tokens.insert(
                tokenDTO = TokenDTO(
                    login = registerReceiveRemote.login,
                    token = token
                )
            )
            call.respond(RegisterResponseRemote(token = token))
            println("Successful register happened: ${registerReceiveRemote.login}")
        } catch (e: Exception) {
            println(e.message)
        }
    }
}