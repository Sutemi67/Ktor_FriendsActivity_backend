import io.ktor.plugin.features.*

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ktor)
    alias(libs.plugins.kotlin.plugin.serialization)
}

group = "apc.appcradle"
version = "1.1.0"

application {
    mainClass = "io.ktor.server.netty.EngineMain"
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

kotlin {
    jvmToolchain(17)
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.host.common)
    implementation(libs.ktor.server.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.ktor.server.auth)
    implementation(libs.ktor.server.netty)
    implementation(libs.logback.classic)
    implementation(libs.ktor.server.config.yaml)
    testImplementation(libs.ktor.server.test.host)
    testImplementation(libs.kotlin.test.junit)

    //BD Exposed & PostgresSQL
    implementation(libs.exposed.core)
    implementation(libs.exposed.jdbc)
    implementation(libs.h2)
    implementation("org.postgresql:postgresql:42.7.7")
}

ktor {
    fatJar {
        archiveFileName.set("server-all.jar")
    }
    docker {
        localImageName.set("delta67admin/friends-activity-backend")
        customBaseImage.set("eclipse-temurin:17-jre")
        portMappings.set(
            listOf(
                DockerPortMapping(6655, 6655)
            )
        )
    }
}