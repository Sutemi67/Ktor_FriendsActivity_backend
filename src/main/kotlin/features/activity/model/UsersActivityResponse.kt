package apc.appcradle.features.activity.model

import kotlinx.serialization.Serializable

@Serializable
data class UsersActivityResponse(
    val friendsList: List<UserActivityRequest>,
    val errorMessage: String?,
    val leader: String?
)