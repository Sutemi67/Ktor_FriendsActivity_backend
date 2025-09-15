package apc.appcradle.database.ahievements

import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.jdbc.update

object Achievements : Table() {
    private val currentLeader = varchar(name = "currentleader", length = 25)

    fun updateLeader(winnerLogin: String) {
        transaction {
            Achievements.update() {
                it[currentLeader] = winnerLogin
            }
        }
    }

    fun getLeader(): String? {
        return try {
            transaction {
                val leader = Achievements.selectAll().single()
                leader[currentLeader]
            }
        } catch (e: Exception) {
            print("Achievements -> cant get leader! Error : ${e.message}")
            return null
        }
    }
}