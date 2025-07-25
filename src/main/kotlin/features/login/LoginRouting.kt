package apc.appcradle.features.login

import apc.appcradle.features.cache.InMemoryCache
import apc.appcradle.features.cache.TokenCache
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.*

fun Application.configureLoginRouting() {
    routing {
        post("/login") {
            val receive = call.receive<LoginReceiveRemote>()
            val firstMatch = InMemoryCache.userList.firstOrNull { it.login == receive.login }

            if (firstMatch == null) {
                call.respond(HttpStatusCode.BadRequest, "User does not exists")
                println("User ${receive.login} does not exists")
            } else {
                if (firstMatch.password == receive.password) {
                    val existingToken = InMemoryCache.token.firstOrNull { it.login == receive.login }
                    if (existingToken != null) {
                        call.respond(LoginResponseRemote(token = existingToken.token))
                        println("User ${receive.login} logged in by token")

                    } else {
                        val token = UUID.randomUUID().toString()
                        InMemoryCache.token.add(TokenCache(login = receive.login, token = token))
                        call.respond(LoginResponseRemote(token = token))
                        println("User ${receive.login} - successful login, got a new token")
                    }
                } else {
                    call.respond(HttpStatusCode.BadRequest, "Password is incorrect")
                    println("User ${receive.login} password incorrect")
                }
            }
        }
    }
}
