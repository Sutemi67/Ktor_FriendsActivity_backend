package apc.appcradle

import apc.appcradle.features.cache.configureUserActivityRouting
import apc.appcradle.features.login.configureLoginRouting
import apc.appcradle.features.register.configureRegisterRouting
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.jetbrains.exposed.v1.jdbc.Database

fun main(args: Array<String>) {
    Database.connect(
//        url = "jdbc:postgresql://192.168.0.12:5432/friends_activity",
        url = "jdbc:postgresql://localhost:5432/friends",
        driver = "org.postgresql.Driver",
        user = "postgres",
//        user = "sergey_sql",
//        password = "t1ued790"
        password = "Uxs5y7rbhnbx4wtv"
    )

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
