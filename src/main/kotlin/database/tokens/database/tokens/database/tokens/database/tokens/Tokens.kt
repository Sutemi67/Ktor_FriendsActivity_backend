package apc.appcradle.database.tokens.database.tokens.database.tokens.database.tokens

import apc.appcradle.database.tokens.database.tokens.TokenDTO
import apc.appcradle.database.users.UserDTO
import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.core.statements.UpsertSqlExpressionBuilder.eq
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.select
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

object Tokens : Table() {
    private val login = varchar(name = "login", length = 25)
    private val token = varchar(name = "token", length = 50)

    fun insert(tokenDTO: TokenDTO) {
        transaction {
            Tokens.insert {
                it[login] = tokenDTO.login
                it[token] = tokenDTO.token
            }
        }
    }
}
