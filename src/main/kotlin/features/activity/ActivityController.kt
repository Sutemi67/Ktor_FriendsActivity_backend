package apc.appcradle.features.activity

import apc.appcradle.database.ahievements.Achievements
import apc.appcradle.database.users.Users
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import kotlinx.coroutines.*
import java.time.*
import java.time.temporal.TemporalAdjusters

class ActivityController() {

    private val schedulerScope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private var weeklyResetJob: Job? = null

    suspend fun updateSteps(call: ApplicationCall) {
        val receive = call.receive<UsersActivity>()
        val userDTO = Users.fetchUser(receive.login)

        if (userDTO == null) {
            call.respond(HttpStatusCode.BadRequest, "User does not exists")
            println("User ${receive.login} does not exists, so cant fetch data")
        } else {
            val newList = Users.loadStepsGetList(
                userDTO.copy(
                    steps = receive.steps,
                    weeklySteps = receive.weeklySteps
                )
            )
            val leader = Achievements.getLeader()
            println("User ${receive.login} fetched data, steps: ${receive.steps}")
            call.respond(
                UserActivityResponse(
                    friendsList = newList,
                    errorMessage = null,
                    leader = leader
                )
            )
        }
    }

    suspend fun loadUserData(call: ApplicationCall) {
        try {
            val receive = call.receive<UserLogin>()
            val data = Users.loadUserData(receive.login)
            call.respond(data)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, message = "Не удалось соединиться с базой данных.")
        }
    }

    fun startWeeklyResetScheduler(application: Application) {
        if (weeklyResetJob != null) return

        val zone: ZoneId = ZoneId.systemDefault()
        weeklyResetJob = schedulerScope.launch {
            while (isActive) {
                val now: ZonedDateTime = ZonedDateTime.now(zone)
                val nextMondayMidnight: ZonedDateTime = now
                    .with(TemporalAdjusters.next(DayOfWeek.MONDAY))
                    .with(LocalTime.MIDNIGHT)

                val delayMillis = Duration.between(now, nextMondayMidnight).toMillis()
                if (delayMillis > 0) {
                    delay(delayMillis)
                }

                try {
                    val updated = Users.resetAllWeeklySteps()
                    println("[WeeklyReset] Reset weeklySteps to 0 for $updated users")
                } catch (e: Exception) {
                    println("[WeeklyReset] Error: ${e.message}")
                }
                delay(Duration.ofDays(7).toMillis())
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