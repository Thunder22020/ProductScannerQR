package com.daniel.scannerqr.services.db

import com.daniel.scannerqr.proverkacheka.models.CheckResponse
import com.daniel.scannerqr.proverkacheka.models.entities.CheckDataEntity

interface CheckApiDatabaseService {
    fun saveCheck(check: CheckResponse?) : CheckResponse
    fun findCheckByQrRaw(qrRaw: String) : CheckDataEntity?
}