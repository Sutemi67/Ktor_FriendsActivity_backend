package apc.appcradle.features.activity.model

import kotlinx.serialization.Serializable

@Serializable
data class UserDataResponse(
    val steps: Int,
    val weeklySteps: Int,
    val errorMessage: String? = null
)