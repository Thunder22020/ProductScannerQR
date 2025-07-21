package com.daniel.scannerqr.services.db

import com.daniel.scannerqr.proverkacheka.models.CheckResponse

interface CheckApiDatabaseService {
    fun saveCheck(check: CheckResponse?) : CheckResponse
}