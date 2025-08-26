package apc.appcradle.database.users

import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.jdbc.update

object Users : Table() {
    private val login = varchar(name = "login", length = 25)
    private val password = varchar(name = "password", length = 25)
    private val steps = integer(name = "steps")
    private val changeLogin = varchar(name = "changeLogin", length = 25)

    fun insert(userDTO: UserDTO) {
        try {
            transaction {
                Users.insert {
                    it[login] = userDTO.login
                    it[password] = userDTO.password
                    it[steps] = userDTO.steps
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
                    steps = userModel[steps]
                )
            }
        } catch (e: Exception) {
            println("fetching data -> ${e.message}")
            null
        }
    }

    fun updateSteps(userDTO: UserDTO) {
        try {
            transaction {
                Users.update({ login eq userDTO.login }) {
                    it[steps] = userDTO.steps
                }
            }
        } catch (e: Exception) {
            println(e.message)
        }
    }

    fun changeLogin(userDTO: UserDTO) {
        try {
            transaction {
                Users.update({ login eq userDTO.login }) {
                    it[login] = userDTO.changeLogin ?: ""
                }
            }
        } catch (e: Exception) {
            println(e.message)
        }
    }
}
