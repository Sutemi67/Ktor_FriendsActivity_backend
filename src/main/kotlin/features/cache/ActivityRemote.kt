package apc.appcradle.features.cache

import kotlinx.serialization.Serializable


@Serializable
data class UsersActivity(
    val login: String,
    val steps: Int,
    val weeklySteps: Int
)

@Serializable
data class UserActivityResponse(
    val friendsList: List<UsersActivity>,
    val errorMessage: String?
)