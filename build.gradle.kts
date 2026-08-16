import io.ktor.plugin.features.*

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ktor)
    alias(libs.plugins.kotlin.plugin.serialization)
    alias(libs.plugins.graalvm.native)
}

group = "apc.appcradle"
version = "1.1.0"

application {
    mainClass = "apc.appcradle.ApplicationKt"
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

kotlin {
    jvmToolchain(21)
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
    implementation(libs.ktor.server.cio)
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
        localImageName.set("delta67admin/fa-backend")
        customBaseImage.set("eclipse-temurin:21-jre-alpine")
        portMappings.set(
            listOf(
                DockerPortMapping(6655, 6655)
            )
        )
    }
}

graalvmNative {
    metadataRepository {
        enabled.set(true)
    }
    binaries {
        named("main") {
            fallback.set(false)
            imageName.set("fa-backend")
            buildArgs.addAll(
                listOf(
                    "--initialize-at-build-time=ch.qos.logback",
                    "--initialize-at-build-time=io.ktor,kotlin",
                    "--initialize-at-build-time=org.slf4j.LoggerFactory",
                    "--initialize-at-build-time=org.slf4j.helpers.Reporter",
                    "--initialize-at-build-time=kotlinx.io.bytestring.ByteString",
                    "--initialize-at-build-time=kotlinx.io.SegmentPool",
                    "--initialize-at-build-time=kotlinx.serialization.json.Json",
                    "--initialize-at-build-time=kotlinx.serialization.json.JsonImpl",
                    "--initialize-at-build-time=kotlinx.serialization.json.ClassDiscriminatorMode",
                    "--initialize-at-build-time=kotlinx.serialization.modules.SerializersModuleKt",
                    "-H:+InstallExitHandlers",
                    "-H:+ReportUnsupportedElementsAtRuntime",
                    "-H:+ReportExceptionStackTraces"
                )
            )
        }
    }
}