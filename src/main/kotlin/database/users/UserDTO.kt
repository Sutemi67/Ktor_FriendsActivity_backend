package apc.appcradle.database.users

data class UserDTO(
    val login: String,
    val password: String,
    val steps: Int,
    val weeklySteps: Int,
    val changeLogin: String? = null
)