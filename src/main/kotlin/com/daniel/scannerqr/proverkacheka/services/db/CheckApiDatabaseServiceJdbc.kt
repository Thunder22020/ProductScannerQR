package com.daniel.scannerqr.proverkacheka.services.db

import com.daniel.scannerqr.proverkacheka.models.CheckResponse
import com.daniel.scannerqr.proverkacheka.models.entities.CheckDataEntity
import com.daniel.scannerqr.proverkacheka.models.entities.ItemEntity
import com.daniel.scannerqr.proverkacheka.utils.CheckApiDtoConverter
import com.daniel.scannerqr.services.db.CheckApiDatabaseService
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException


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

        val entity = CheckApiDtoConverter.fromDto(check.data.json, check.request.qrraw)
        val connection = getConnection()

        try {
            connection.autoCommit = false

            val checkSql = """
                INSERT INTO checks (user_name, user_inn, operator, total_sum, qr_raw)
                VALUES (?, ?, ?, ?, ?) RETURNING id
            """.trimIndent()

            val checkId: Long
            connection.prepareStatement(checkSql).use { statement ->
                statement.setString(1, entity.user)
                statement.setString(2, entity.userInn)
                statement.setString(3, entity.operator)
                statement.setDouble(4, entity.totalSum ?: 0.0)
                statement.setString(5, entity.qrRaw)

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
            """.trimIndent()

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

    override fun findCheckByQrRaw(qrRaw: String): CheckDataEntity? {
        val connection = getConnection()

        try {
            val checkSql = """
            SELECT * FROM checks
            WHERE qr_raw = ?
        """.trimIndent()

            connection.prepareStatement(checkSql).use { statement ->
                statement.setString(1, qrRaw.lowercase())
                val rs = statement.executeQuery()

                if (!rs.next()) return null

                val checkId = rs.getLong("id")
                val user = rs.getString("user_name")
                val userInn = rs.getString("user_inn")
                val operator = rs.getString("operator")
                val totalSum = rs.getDouble("total_sum")
                val qr = rs.getString("qr_raw")

                val itemsSql = """
                SELECT * FROM items
                WHERE check_id = ?
            """.trimIndent()

                val items = mutableListOf<ItemEntity>()

                connection.prepareStatement(itemsSql).use { itemStmt ->
                    itemStmt.setLong(1, checkId)
                    val itemRs = itemStmt.executeQuery()

                    while (itemRs.next()) {
                        val item = ItemEntity(
                            id = itemRs.getLong("id"),
                            name = itemRs.getString("name"),
                            price = itemRs.getDouble("price"),
                            quantity = itemRs.getInt("quantity"),
                            check = CheckDataEntity(
                                id = checkId,
                                items = mutableListOf(),
                                user = null,
                                userInn = null,
                                operator = null,
                                totalSum = null,
                                qrRaw = null
                            )
                        )
                        items.add(item)
                    }
                }

                val check = CheckDataEntity(
                    id = checkId,
                    items = items,
                    user = user,
                    userInn = userInn,
                    operator = operator,
                    totalSum = totalSum,
                    qrRaw = qr
                )

                items.forEach { it.check = check }

                return check
            }
        } catch (ex: SQLException) {
            connection.rollback()
            throw ex
        } finally {
            connection.close()
        }
    }

    private fun getConnection(
    ): Connection {
        return DriverManager.getConnection(url, username, password)
    }
}
