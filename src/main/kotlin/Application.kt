package apc.appcradle

import apc.appcradle.features.cache.configureUserActivityRouting
import apc.appcradle.features.login.configureLoginRouting
import apc.appcradle.features.register.configureRegisterRouting
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    embeddedServer(
        factory = Netty,
        port = 5555,
        host = "0.0.0.0"
    ) {
        module()
    }.start(wait = true)

}


fun Application.module() {
    configureSerialization()
    configureSecurity()
    configureRegisterRouting()
    configureLoginRouting()
    configureUserActivityRouting()
    configureRouting()
}
