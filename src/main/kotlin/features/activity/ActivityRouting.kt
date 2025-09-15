package apc.appcradle.features.activity

import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureUserActivityRouting() {
    val controller = ActivityController()
    controller.startWeeklyResetScheduler(this)

    routing {
        post("/post_activity") {
            controller.updateSteps(call)
        }
    }
}