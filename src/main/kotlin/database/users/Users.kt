package apc.appcradle.database.users

import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.select
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

object Users : Table() {
    private val login = varchar(name = "login", length = 25)
    private val password = varchar(name = "password", length = 25)

    fun insert(userDTO: UserDTO) {
        transaction {
            Users.insert {
                it[login] = userDTO.login
                it[password] = userDTO.password
            }
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
                )
            }
        } catch (e: Exception) {
            println(e.message)
            null
        }
    }
}
