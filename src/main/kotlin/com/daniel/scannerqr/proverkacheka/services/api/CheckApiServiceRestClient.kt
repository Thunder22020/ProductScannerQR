package com.daniel.scannerqr.proverkacheka.services.api

import com.daniel.scannerqr.proverkacheka.models.CheckResponse
import com.daniel.scannerqr.proverkacheka.utils.QrUtils
import com.daniel.scannerqr.services.api.CheckApiService
import com.daniel.scannerqr.services.db.CheckApiDatabaseService
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestClient
import org.springframework.web.multipart.MultipartFile

@Service
@ConditionalOnProperty(name = ["scanner_qr.service-type.api"], havingValue = "rest_client")
class CheckApiServiceRestClient(
    private val client: RestClient,
    private val dbService: CheckApiDatabaseService,
    @Value("\${api.check.token}") private val token: String,
) : CheckApiService {
    override fun getCheckResponse(qrRaw: String): CheckResponse? {
        val formData: MultiValueMap<String, String> = LinkedMultiValueMap()
        formData.add("token", token)
        formData.add("qrraw", qrRaw)

        val result = client.post()
            .uri("/check/get")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(formData)
            .retrieve()
            .body(CheckResponse::class.java)

        dbService.saveCheck(result)

        return result
    }

    override fun getCheckResponse(qrFile: MultipartFile): CheckResponse? {
        val qrRaw : String? = QrUtils.decodeQrFromImage(qrFile)

        if (qrRaw == null) {
            throw RuntimeException("QR is null")
        }

        return getCheckResponse(qrRaw)
    }

}