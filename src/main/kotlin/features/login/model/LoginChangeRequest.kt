package apc.appcradle.features.login.model

import kotlinx.serialization.Serializable

@Serializable
data class LoginChangeRequest(
    val login: String,
    val newLogin: String
)