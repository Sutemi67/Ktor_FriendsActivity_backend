package apc.appcradle.features.register.model

import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequest(
    val login: String,
    val password: String
)