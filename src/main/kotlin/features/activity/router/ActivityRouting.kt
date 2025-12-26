package apc.appcradle.features.activity.router

import apc.appcradle.features.activity.data.ActivityRepository
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureUserActivityRouting() {
    val controller = ActivityRepository()
    controller.startWeeklyResetScheduler(this)

    routing {
        post("/post_activity") {
            controller.updateSteps(call)
        }
        post("/fetch") {
            controller.getUserData(call)
        }
    }
}