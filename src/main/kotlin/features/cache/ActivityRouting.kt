package apc.appcradle.features.cache

import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureUserActivityRouting() {
    routing {
        post("/post_activity") {
            val controller = ActivityController()
            controller.updateSteps(call)
        }
    }
}