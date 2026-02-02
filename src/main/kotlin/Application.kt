package apc.appcradle

import apc.appcradle.features.activity.router.configureUserActivityRouting
import apc.appcradle.features.login.router.configureLoginRouting
import apc.appcradle.features.register.router.configureRegisterRouting
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import org.jetbrains.exposed.v1.jdbc.Database

fun main(args: Array<String>) {
    try {
        with(ServerPasswords) {
            Database.connect(
                url = FROM_DOCKER_URL,
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
        install(ContentNegotiation) {
            json()
        }
        connectModules()
    }.start(wait = true)
}

fun Application.connectModules() {
    configureRegisterRouting()
    configureLoginRouting()
    configureUserActivityRouting()
}