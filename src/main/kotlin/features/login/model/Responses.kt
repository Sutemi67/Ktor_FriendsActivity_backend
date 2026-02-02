package apc.appcradle.features.login.model

import kotlinx.serialization.Serializable

interface Responses {
    @Serializable
    data class LoginResponse(
        val token: String
    ) : Responses

    @Serializable
    data class LoginChangeResponse(
        val message: String
    ) : Responses
}