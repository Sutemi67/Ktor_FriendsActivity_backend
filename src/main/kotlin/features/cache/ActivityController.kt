package apc.appcradle.features.cache

import apc.appcradle.database.users.Users
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*

class ActivityController() {

    suspend fun updateSteps(call: ApplicationCall) {
        val receive = call.receive<UsersActivity>()
        val userDTO = Users.fetchUser(receive.login)

        if (userDTO == null) {
            call.respond(HttpStatusCode.BadRequest, "User does not exists")
            println("User ${receive.login} does not exists, so cant fetch data")
        } else {
            val newList = Users.loadStepsGetList(userDTO.copy(steps = receive.steps))
            println("User ${receive.login} fetched data, steps: ${receive.steps}")
            call.respond(
                UserActivityResponse(
                    friendsList = newList,
                    errorMessage = null
                )
            )
        }
    }
}