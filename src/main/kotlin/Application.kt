package apc.appcradle

import apc.appcradle.features.cache.configureUserActivityRouting
import apc.appcradle.features.login.configureLoginRouting
import apc.appcradle.features.register.configureRegisterRouting
import io.ktor.server.application.*
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.*

fun main(args: Array<String>) {
    embeddedServer(
        factory = Netty,
        port = 6655,
        host = "0.0.0.0"
    ) {
        module()
    }.start(wait = true)
//    EngineMain.main(args)
}


fun Application.module() {
    configureSerialization()
    configureSecurity()
    configureRegisterRouting()
    configureLoginRouting()
    configureUserActivityRouting()
    configureRouting()
}
