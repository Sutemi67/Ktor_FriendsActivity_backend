package apc.appcradle.features.activity.model

import kotlinx.serialization.Serializable

@Serializable
data class UsersActivityResponse(
    val friendsList: List<UserFetchActivityRequest>,
    val errorMessage: String?,
    val leader: String?
)