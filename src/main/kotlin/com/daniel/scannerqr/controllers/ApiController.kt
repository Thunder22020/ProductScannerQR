package com.daniel.scannerqr.controllers

import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import com.daniel.scannerqr.services.ApiService
import com.daniel.scannerqr.models.CheckResponse
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestMapping

@RestController
@RequestMapping("/api/qr")
class ApiController(
    private val service: ApiService,
) {

    @PostMapping("/upload", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun uploadImage(@RequestParam image: MultipartFile): ResponseEntity<CheckResponse?> {
        val result = service.makeRequest(image)
        return ResponseEntity.ok(result)
    }

}