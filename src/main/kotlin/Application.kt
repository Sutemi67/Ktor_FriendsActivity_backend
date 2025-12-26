package apc.appcradle

import apc.appcradle.common_modules.configureSerialization
import apc.appcradle.features.activity.router.configureUserActivityRouting
import apc.appcradle.features.login.router.configureLoginRouting
import apc.appcradle.features.register.router.configureRegisterRouting
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.jetbrains.exposed.v1.jdbc.Database


fun main(args: Array<String>) {
    try {
        with(ServerPasswords) {
            Database.connect(
                url = URL,
                driver = DRIVER,
                user = USER,
                password = PASSWORD
            )
        }
    } catch (e: Exception) {
        println("cant connect to database -> ${e.message}")
    }

    embeddedServer(
        factory = Netty,
        port = 6655,
        host = "0.0.0.0"
    ) {
        connectModules()
    }.start(wait = true)
}

fun Application.connectModules() {
    configureSerialization()
    configureRegisterRouting()
    configureLoginRouting()
    configureUserActivityRouting()
}