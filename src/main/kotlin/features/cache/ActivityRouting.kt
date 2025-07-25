package apc.appcradle.features.cache

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureUserActivityRouting() {
    routing {
        post("/post_activity") {
            val receive = call.receive<UserActivity>()
            val firstMatch = InMemoryCache.userList.firstOrNull { it.login == receive.login }

            if (firstMatch == null) {
                call.respond(HttpStatusCode.BadRequest, "User does not exists")
                println("User ${receive.login} does not exists, so cant fetch data")
            } else {
                val user = InMemoryCache.userActivity.indexOfFirst { it.login == receive.login }
                if (user != -1) {
                    InMemoryCache.userActivity[user] =
                        UserActivity(login = receive.login, steps = receive.steps)
                    println("User ${receive.login} fetched data, steps: ${receive.steps}")
                } else {
                    InMemoryCache.userActivity.add(UserActivity(login = receive.login, steps = receive.steps))
                    println("User ${receive.login} created his data")
                }
                call.respond(
                    UserActivityResponse(
                        friendsList = InMemoryCache.userActivity,
                        errorMessage = null
                    )
                )
            }
        }
    }
}