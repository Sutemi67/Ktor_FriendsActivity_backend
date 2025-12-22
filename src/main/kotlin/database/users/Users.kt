package apc.appcradle.database.users

import apc.appcradle.database.ahievements.CurrentLeader
import apc.appcradle.features.activity.UsersActivity
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

    fun insert(userDTO: UserDTO) {
        try {
            transaction {
                Users.insert {
                    it[login] = userDTO.login
                    it[password] = userDTO.password
                    it[steps] = userDTO.steps
                    it[weeklySteps] = userDTO.weeklySteps
                }
            }
        } catch (e: Exception) {
            println("Users.insert -> ${e.message}")
        }
    }

    fun fetchUser(login: String): UserDTO? {
        return try {
            transaction {
                val userModel = Users.selectAll()
                    .where { Users.login eq login }
                    .single()
                UserDTO(
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

    fun loadUserData(login: String): FetchedData {
        return try {
            val user = fetchUser(login)
            if (user != null) {
                val whoLeader = CurrentLeader.getLeader()
                FetchedData(
                    steps = user.steps,
                    weeklySteps = user.weeklySteps,
                    currentLeader = whoLeader,
                )
            } else {
                FetchedData(
                    steps = 0,
                    weeklySteps = 0,
                    errorMessage = "User not found"
                )
            }
        } catch (e: Exception) {
            FetchedData(
                steps = 0,
                weeklySteps = 0,
                errorMessage = e.message
            )
        }
    }

    fun loadStepsGetList(userDTO: UserDTO): List<UsersActivity> {
        try {
            var list = emptyList<UsersActivity>()
            transaction {
                Users.update({ login eq userDTO.login }) {
                    it[steps] = userDTO.steps
                    it[weeklySteps] = userDTO.weeklySteps
                }
            }
            transaction {
                val userList = Users.selectAll().toList()
                list = userList.map {
                    UsersActivity(
                        login = it[login],
                        steps = it[steps],
                        weeklySteps = it[weeklySteps],
                    )
                }.sortedByDescending { it.weeklySteps }
            }
            return list
        } catch (e: Exception) {
            println("Users.kt, loadStepsGetList: ${userDTO.login}, ${e.message}")
            return emptyList()
        }
    }

    fun changeLogin(userDTO: UserDTO): String? {
        return try {
            transaction {
                Users.update({ login eq userDTO.login }) {
                    it[login] = userDTO.changeLogin ?: ""
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
    fun resetAllWeeklySteps() {
        try {
            transaction {
                val leader = Users
                    .selectAll()
                    .orderBy(weeklySteps to SortOrder.DESC, login to SortOrder.ASC)
                    .firstOrNull()

                leader?.let {
                    CurrentLeader.updateLeader(it[login])
                }

                Users.update({ weeklySteps neq 0 }) {
                    it[weeklySteps] = 0
                }
            }
            println("Users.kt, resetAllWeeklySteps -> Weekly steps reset and leader updated successfully")
        } catch (e: Exception) {
            println("Users.kt, resetAllWeeklySteps -> Error: ${e.message}")
        }
    }
}
