package apc.appcradle.features.activity.model

data class UserSQL(
    val login: String,
    val password: String,
    val steps: Int,
    val weeklySteps: Int,
    val changeLogin: String? = null
)