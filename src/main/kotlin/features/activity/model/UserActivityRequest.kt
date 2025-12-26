package apc.appcradle.features.activity.model

import kotlinx.serialization.Serializable

@Serializable
data class UserActivityRequest(
    val login: String,
    val steps: Int,
    val weeklySteps: Int,
)