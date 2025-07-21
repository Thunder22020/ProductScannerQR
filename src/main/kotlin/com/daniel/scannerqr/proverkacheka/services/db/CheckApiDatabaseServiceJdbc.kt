package com.daniel.scannerqr.proverkacheka.services.db

import com.daniel.scannerqr.proverkacheka.models.CheckResponse
import com.daniel.scannerqr.proverkacheka.utils.DtoToEntityConverter
import com.daniel.scannerqr.services.db.CheckApiDatabaseService
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.sql.Connection
import java.sql.DriverManager
import java.sql.Statement


@Service
@Transactional
@ConditionalOnProperty(name = ["scanner_qr.service-type.db"], havingValue = "jdbc")
class CheckApiDatabaseServiceJdbc(
    @Value("\${spring.datasource.url}") private val url: String,
    @Value("\${spring.datasource.username}") private val username: String,
    @Value("\${spring.datasource.password}") private val password: String,
) : CheckApiDatabaseService {

    override fun saveCheck(check: CheckResponse?): CheckResponse {
        if (check == null) throw RuntimeException("Check is null")

        val entity = DtoToEntityConverter.fromDto(check.data.json)
        val connection = getConnection()

        try {
            connection.autoCommit = false

            val checkSql = """
                INSERT INTO checks (user_name, user_inn, operator, total_sum)
                VALUES (?, ?, ?, ?) RETURNING id
            """

            val checkId: Long
            connection.prepareStatement(checkSql).use { statement ->
                statement.setString(1, entity.user)
                statement.setString(2, entity.userInn)
                statement.setString(3, entity.operator)
                statement.setDouble(4, entity.totalSum ?: 0.0)

                val rs = statement.executeQuery()
                if (rs.next()) {
                    checkId = rs.getLong("id")
                } else {
                    throw RuntimeException("Failed to insert check and retrieve ID")
                }
            }

            val itemSql = """
                INSERT INTO items (check_id, name, price, quantity)
                VALUES (?, ?, ?, ?)
            """

            connection.prepareStatement(itemSql).use { statement ->
                for (item in entity.items) {
                    statement.setLong(1, checkId)
                    statement.setString(2, item.name)
                    statement.setDouble(3, item.price)
                    statement.setInt(4, item.quantity)
                    statement.addBatch()
                }
                statement.executeBatch()
            }

            connection.commit()
            return check
        } catch (_: Exception) {
            connection.rollback()
            throw RuntimeException("Failed to save check using JDBC")
        } finally {
            connection.close()
        }
    }

    private fun getConnection(
    ): Connection {
        return DriverManager.getConnection(url, username, password)
    }
}
