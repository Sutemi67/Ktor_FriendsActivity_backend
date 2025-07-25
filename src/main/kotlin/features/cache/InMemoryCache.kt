package apc.appcradle.features.cache

import apc.appcradle.features.register.RegisterReceiveRemote
import kotlinx.serialization.Serializable

data class TokenCache(
    val login: String,
    val token: String
)

object InMemoryCache {
    val userList: MutableList<RegisterReceiveRemote> = mutableListOf()
    val token: MutableList<TokenCache> = mutableListOf()
    val userActivity: MutableList<UserActivity> = mutableListOf()
}



