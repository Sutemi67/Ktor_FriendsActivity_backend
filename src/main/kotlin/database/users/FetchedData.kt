package apc.appcradle.database.users

data class FetchedData(
    val steps: Int,
    val weeklySteps: Int,
    val currentLeader: String? = null,
    val errorMessage: String? = null
)
