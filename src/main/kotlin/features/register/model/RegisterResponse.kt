package apc.appcradle.features.register.model

import kotlinx.serialization.Serializable

@Serializable
data class RegisterResponse(
    val token: String
)