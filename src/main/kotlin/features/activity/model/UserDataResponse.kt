package apc.appcradle.features.activity.model

import kotlinx.serialization.Serializable

@Serializable
data class UserDataResponse(
    val steps: Int? = null,
    val weeklySteps: Int? = null,
    val errorMessage: String? = null
)