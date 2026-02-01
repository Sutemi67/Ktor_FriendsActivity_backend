package apc.appcradle.features.activity.data

import apc.appcradle.features.activity.model.UserFetchActivityRequest
import apc.appcradle.features.activity.model.UserSQL
import org.jetbrains.exposed.v1.core.SortOrder
import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.core.statements.UpsertSqlExpressionBuilder.greater
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.select
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.jdbc.update

object Users : Table() {
    private val login = varchar(name = "login", length = 25)
    private val password = varchar(name = "password", length = 25)
    private val steps = integer(name = "steps")
    private val weeklySteps = integer(name = "weeklysteps")
    private val changeLogin = varchar(name = "changelogin", length = 25)

    fun insert(userSQL: UserSQL) {
        try {
            transaction {
                Users.insert {
                    it[login] = userSQL.login
                    it[password] = userSQL.password
                    it[steps] = userSQL.steps
                    it[weeklySteps] = userSQL.weeklySteps
                }
            }
        } catch (e: Exception) {
            println("Users.insert -> ${e.message}")
        }
    }

    fun getUserData(login: String): UserSQL? {
        return transaction {
            Users.select(Users.login)
                .where { Users.login eq login }
                .map { userModel ->
                    UserSQL(
                        login = userModel[Users.login],
                        password = userModel[password],
                        steps = userModel[steps],
                        weeklySteps = userModel[weeklySteps]
                    )
                }
                .singleOrNull()
        }
    }


    fun loadUserStepsGetActivityList(userSQL: UserSQL): List<UserFetchActivityRequest> {
        try {
            var list = emptyList<UserFetchActivityRequest>()
            transaction {
                Users.update({ login eq userSQL.login }) {
                    it[steps] = userSQL.steps
                    it[weeklySteps] = userSQL.weeklySteps
                }
            }
            transaction {
                val userList = Users.selectAll().toList()
                list = userList.map {
                    UserFetchActivityRequest(
                        login = it[login],
                        steps = it[steps],
                        weeklySteps = it[weeklySteps],
                    )
                }.sortedByDescending { it.weeklySteps }
            }
            return list
        } catch (e: Exception) {
            println("Users.kt, loadStepsGetList: ${userSQL.login}, ${e.message}")
            return emptyList()
        }
    }

    fun changeLogin(userSQL: UserSQL): String? {
        return try {
            transaction {
                Users.update({ login eq userSQL.login }) {
                    it[login] = userSQL.changeLogin ?: ""
                }
            }
            null
        } catch (e: Exception) {
            println(e.message)
            "${e.message}"
        }
    }

    fun resetAllWeeklySteps(): Int = transaction {
        try {
            val leader = Users
                .select(weeklySteps greater 0)
                .orderBy(weeklySteps to SortOrder.DESC)
                .firstOrNull()

            leader?.let {
                CurrentLeader.updateLeader(it[login])
            }

            val updatedCount = Users.update({ weeklySteps neq 0 }) {
                it[weeklySteps] = 0
            }
            println("Users.kt, resetAllWeeklySteps -> Weekly steps reset and leader updated successfully")
            updatedCount
        } catch (e: Exception) {
            println("Users.kt, resetAllWeeklySteps -> Error: ${e.message}")
            0
        }
    }
}