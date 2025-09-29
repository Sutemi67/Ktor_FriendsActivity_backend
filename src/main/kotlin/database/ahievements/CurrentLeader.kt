package apc.appcradle.database.ahievements

import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.jdbc.update

object CurrentLeader : Table() {
    private val currentLeader = varchar(name = "currentleader", length = 25)

    // todo проверить на вставку нового победителя на новом сервере, а так же проверить авто обновление
    fun updateLeader(winnerLogin: String) {
        transaction {
            CurrentLeader.update {
                it[currentLeader] = winnerLogin
            }
        }
    }

    fun getLeader(): String? {
        return try {
            transaction {
                val leader = CurrentLeader.selectAll().singleOrNull()
                if (leader != null) leader[currentLeader] else null
            }
        } catch (e: Exception) {
            print("Achievements -> cant get leader! Error : ${e.message}")
            return null
        }
    }
}
