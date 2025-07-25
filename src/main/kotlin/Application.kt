package apc.appcradle

import apc.appcradle.features.cache.configureUserActivityRouting
import apc.appcradle.features.login.configureLoginRouting
import apc.appcradle.features.register.configureRegisterRouting
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}


fun Application.module() {
    configureSerialization()
    configureSecurity()
    configureRegisterRouting()
    configureLoginRouting()
    configureUserActivityRouting()
    configureRouting()
}
