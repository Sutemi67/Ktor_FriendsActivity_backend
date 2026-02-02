package apc.appcradle.features.login.model

import kotlinx.serialization.Serializable

interface Requests {
    @Serializable
    data class LoginChangeRequest(
        val login: String,
        val newLogin: String
    ) : Requests

    @Serializable
    data class LoginRequest(
        val login: String,
        val password: String
    ) : Requests
}