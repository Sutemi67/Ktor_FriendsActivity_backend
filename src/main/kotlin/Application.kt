package apc.appcradle

import apc.appcradle.features.cache.configureUserActivityRouting
import apc.appcradle.features.login.configureLoginRouting
import apc.appcradle.features.register.configureRegisterRouting
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
