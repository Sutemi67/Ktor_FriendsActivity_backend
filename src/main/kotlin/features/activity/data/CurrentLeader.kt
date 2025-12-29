package apc.appcradle.features.activity.data

import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.jdbc.update

object CurrentLeader : Table() {
    private val currentLeader = varchar(name = "currentleader", length = 25)
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