package apc.appcradle.features.cache

import kotlinx.serialization.Serializable


@Serializable
data class UserActivity(
    val login: String,
    val steps: Int
)

@Serializable
data class UserActivityResponse(
    val friendsList: List<UserActivity>,
    val errorMessage: String?
)