package com.daniel.scannerqr.services

import org.springframework.http.MediaType
import com.daniel.scannerqr.utils.QrUtils
import org.springframework.util.MultiValueMap
import org.springframework.stereotype.Service
import com.daniel.scannerqr.models.CheckResponse
import org.springframework.web.client.RestClient
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.multipart.MultipartFile
import com.daniel.scannerqr.utils.DtoToEntityConverter
import com.daniel.scannerqr.repositories.ItemRepository
import org.springframework.beans.factory.annotation.Value

@Service
class ApiService(
    private val restClient: RestClient,
    @Value("\${api.check.token}") private val token: String,
    private val itemRepository: ItemRepository
) {

    fun makeRequest(qrRaw: String): CheckResponse? {
        val formData: MultiValueMap<String, String> = LinkedMultiValueMap()
        formData.add("token", token)
        formData.add("qrraw", qrRaw)

        val result = restClient.post()
            .uri("https://proverkacheka.com/api/v1/check/get")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(formData)
            .retrieve()
            .body(CheckResponse::class.java)

        saveCheck(result)

        return result
    }

    fun makeRequest(qrFile: MultipartFile): CheckResponse? {
        val qrRaw : String? = QrUtils.decodeQrFromImage(qrFile)

        if (qrRaw == null) {
            throw RuntimeException("QR is null")
        }

        return makeRequest(qrRaw)
    }

    fun saveCheck(check: CheckResponse?) {
        if (check == null) {
            throw RuntimeException("Check is null")
        }

        val entity = DtoToEntityConverter.fromDto(check.data.json)
        itemRepository.saveAll(entity.items)
    }
}
