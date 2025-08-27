package apc.appcradle.features.cache

import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureUserActivityRouting() {
    val controller = ActivityController()

    routing {
        post("/post_activity") {
            controller.updateSteps(call)
        }
    }
}