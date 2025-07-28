package com.daniel.scannerqr.proverkacheka.repositories

import com.daniel.scannerqr.proverkacheka.models.CheckResponse
import com.daniel.scannerqr.proverkacheka.models.entities.CheckDataEntity
import org.springframework.data.jpa.repository.JpaRepository

interface CheckRepository : JpaRepository<CheckDataEntity, Long> {
    fun findCheckByQrRaw(qrRaw: String): CheckDataEntity?
}