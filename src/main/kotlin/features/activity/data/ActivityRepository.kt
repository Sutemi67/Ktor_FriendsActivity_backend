package apc.appcradle.features.activity.data

import apc.appcradle.features.activity.model.UserActivityRequest
import apc.appcradle.features.activity.model.UserDataResponse
import apc.appcradle.features.activity.model.UsersActivityResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import kotlinx.coroutines.*
import java.time.*
import java.time.temporal.TemporalAdjusters

class ActivityRepository() {

    private val schedulerScope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private var weeklyResetJob: Job? = null

    suspend fun updateSteps(call: ApplicationCall) {
        val receive = call.receive<UserActivityRequest>()
        val userSQL = Users.getUserData(receive.login)

        if (userSQL == null) {
            call.respond(HttpStatusCode.BadRequest, "User does not exists")
            println("User ${receive.login} does not exists, so cant fetch data")
        } else {
            val newList = Users.loadUserStepsGetActivityList(
                userSQL.copy(
                    steps = receive.steps,
                    weeklySteps = receive.weeklySteps
                )
            )
            val leader = CurrentLeader.getLeader()
            println("User ${receive.login} fetched data, steps: ${receive.steps}")
            call.respond(
                UsersActivityResponse(
                    friendsList = newList,
                    errorMessage = null,
                    leader = leader
                )
            )
        }
    }

    suspend fun getUserData(call: ApplicationCall) {

        val receive = call.receive<UserActivityRequest>()
        val userSQL = Users.getUserData(receive.login)

        try {
            if (userSQL == null) {
                call.respond(
                    UserDataResponse(
                        steps = 0,
                        weeklySteps = 0,
                        errorMessage = "User not found"
                    )
                )
            } else {
                call.respond(
                    UserDataResponse(
                        steps = userSQL.steps,
                        weeklySteps = userSQL.weeklySteps,
                        errorMessage = null
                    )
                )
            }
        } catch (e: Exception) {
            call.respond(
                UserDataResponse(
                    steps = 0,
                    weeklySteps = 0,
                    errorMessage = "Connecting error. ${e.message}"
                )
            )
        }
    }

    fun startWeeklyResetScheduler(application: Application) {
        if (weeklyResetJob?.isActive == true) {
            println("WeeklyReset -> Scheduler already running!")
            return
        }
        if (weeklyResetJob != null) return

        val zone: ZoneId = ZoneId.systemDefault()
        weeklyResetJob = schedulerScope.launch {
            while (isActive) {
                val now = ZonedDateTime.now(zone)
                val nextMondayMidnight = now
                    .with(TemporalAdjusters.next(DayOfWeek.MONDAY))
                    .with(LocalTime.MIDNIGHT)

                val delayMillis = Duration.between(now, nextMondayMidnight).toMillis().coerceAtLeast(0)
                delay(delayMillis)

                val updatedCount = Users.resetAllWeeklySteps()
                println("WeeklyReset -> Reset weeklySteps for $updatedCount users")
            }
        }

        application.environment.monitor.subscribe(ApplicationStopping) {
            stopWeeklyResetScheduler()
        }
    }

    fun stopWeeklyResetScheduler() {
        weeklyResetJob?.cancel()
        weeklyResetJob = null
    }
}