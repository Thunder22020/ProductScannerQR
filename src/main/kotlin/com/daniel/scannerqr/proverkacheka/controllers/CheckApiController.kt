package com.daniel.scannerqr.proverkacheka.controllers

import com.daniel.scannerqr.proverkacheka.models.CheckResponse
import com.daniel.scannerqr.services.api.CheckApiService
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/qr")
class CheckApiController(
    private val service: CheckApiService
) {

    @PostMapping("/upload", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun uploadImage(@RequestParam image: MultipartFile): ResponseEntity<CheckResponse?> {
        val result = service.getCheckResponse(image)
        return ResponseEntity.ok(result)
    }

}