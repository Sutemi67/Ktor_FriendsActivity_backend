package apc.appcradle.features.activity

import kotlinx.serialization.Serializable

@Serializable
data class UsersActivity(
    val login: String,
    val steps: Int,
    val weeklySteps: Int,
//    val wasBestUserTimes: Int
)

@Serializable
data class UserLogin(
    val login: String,
)

@Serializable
data class UserActivityResponse(
    val friendsList: List<UsersActivity>,
    val errorMessage: String?,
    val leader: String?
)