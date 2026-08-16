package apc.appcradle

import apc.appcradle.features.activity.router.configureUserActivityRouting
import apc.appcradle.features.login.router.configureLoginRouting
import apc.appcradle.features.register.router.configureRegisterRouting
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import io.ktor.server.plugins.contentnegotiation.*
import org.jetbrains.exposed.v1.jdbc.Database

fun main(args: Array<String>) {
    val dbUrl = System.getenv("DB_URL") ?: "jdbc:postgresql://172.17.0.1:5432/friends_activity"
    val dbDriver = System.getenv("DB_DRIVER") ?: "org.postgresql.Driver"
    val dbUser = System.getenv("DB_USER")
    val dbPassword = System.getenv("DB_PASSWORD")

    try {
        Database.connect(
            url = dbUrl,
            driver = dbDriver,
            user = dbUser,
            password = dbPassword
        )
    } catch (e: Exception) {
        println("cant connect to database -> ${e.message}")
    }

    embeddedServer(
        factory = CIO,
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