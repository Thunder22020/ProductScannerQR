package com.daniel.scannerqr.services.api

import com.daniel.scannerqr.proverkacheka.models.CheckResponse
import org.springframework.web.multipart.MultipartFile

interface CheckApiService {
    fun getCheckResponse(qrRaw: String) : CheckResponse?
    fun getCheckResponse(qrFile: MultipartFile) : CheckResponse?
}