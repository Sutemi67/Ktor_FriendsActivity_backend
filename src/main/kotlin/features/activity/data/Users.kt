package apc.appcradle.features.activity.data

import apc.appcradle.features.activity.model.UserSQL
import apc.appcradle.features.activity.model.UserActivityRequest
import org.jetbrains.exposed.v1.core.SortOrder
import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.jdbc.insert
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
        return try {
            transaction {
                val userModel = Users.selectAll()
                    .where { Users.login eq login }
                    .single()
                UserSQL(
                    login = userModel[Users.login],
                    password = userModel[password],
                    steps = userModel[steps],
                    weeklySteps = userModel[weeklySteps]
                )
            }
        } catch (e: Exception) {
            println("Users.kt, fetchUser -> login: $login, error: ${e.message}")
            null
        }
    }

    fun loadUserStepsGetActivityList(userSQL: UserSQL): List<UserActivityRequest> {
        try {
            var list = emptyList<UserActivityRequest>()
            transaction {
                Users.update({ login eq userSQL.login }) {
                    it[steps] = userSQL.steps
                    it[weeklySteps] = userSQL.weeklySteps
                }
            }
            transaction {
                val userList = Users.selectAll().toList()
                list = userList.map {
                    UserActivityRequest(
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

    //    fun resetAllWeeklySteps() {
//        try {
//            transaction {
//                val userList = Users.selectAll().toList()
//                val bestUserWeekly = userList.map { user ->
//                    UsersActivity(
//                        login = user[login],
//                        steps = user[steps],
//                        weeklySteps = user[weeklySteps],
//                    )
//                }.maxByOrNull { it.weeklySteps }
//
//                if (bestUserWeekly != null) {
//                    CurrentLeader.updateLeader(bestUserWeekly.login)
//                }
//
//                Users.update {
//                    it[weeklySteps] = 0
//                }
//            }
//            println("Users.kt, resetAllWeeklySteps -> All weekly data has cleared successfully")
//        } catch (e: Exception) {
//            println("Users.kt, resetAllWeeklySteps -> ${e.message}")
//        }
//    }
    fun resetAllWeeklySteps(): Int = transaction {
        try {
            val leader = Users
                .selectAll()
                .orderBy(weeklySteps to SortOrder.DESC, login to SortOrder.ASC)
                .firstOrNull()

            leader?.let {
                CurrentLeader.updateLeader(it[login])
            }

            val updatedCount = Users.update({ weeklySteps neq 0 }) {
                it[Users.weeklySteps] = 0
            }
            println("Users.kt, resetAllWeeklySteps -> Weekly steps reset and leader updated successfully")
            updatedCount
        } catch (e: Exception) {
            println("Users.kt, resetAllWeeklySteps -> Error: ${e.message}")
            0
        }
    }
}