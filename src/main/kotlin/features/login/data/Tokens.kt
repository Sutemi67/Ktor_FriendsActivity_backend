package apc.appcradle.features.login.data

import apc.appcradle.features.login.model.TokenDTO
import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.jdbc.upsert

object Tokens : Table() {
    private val login = varchar(name = "login", length = 25)
    private val token = varchar(name = "token", length = 50)

    fun insert(tokenDTO: TokenDTO) {
        transaction {
            Tokens.upsert(
                login,
                onUpdate = { it[token] = tokenDTO.token },
                where = { login eq tokenDTO.login }
            ) {
                it[login] = tokenDTO.login
                it[token] = tokenDTO.token
            }
        }
    }
}