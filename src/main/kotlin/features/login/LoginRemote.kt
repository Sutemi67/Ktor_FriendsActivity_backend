package apc.appcradle.features.login

import kotlinx.serialization.Serializable

@Serializable
data class LoginReceiveRemote(
    val login: String,
    val password: String
)

@Serializable
data class LoginResponseRemote(
    val token: String
)

@Serializable
data class LoginChange(
    val login: String,
    val newLogin: String
)