package com.daniel.scannerqr.proverkacheka.services.db

import com.daniel.scannerqr.proverkacheka.models.CheckResponse
import com.daniel.scannerqr.proverkacheka.models.entities.CheckDataEntity
import com.daniel.scannerqr.proverkacheka.repositories.CheckRepository
import com.daniel.scannerqr.proverkacheka.utils.CheckApiDtoConverter
import com.daniel.scannerqr.services.db.CheckApiDatabaseService
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Primary
@Transactional
@ConditionalOnProperty(name = ["scanner_qr.service-type.db"], havingValue = "jpa")
class CheckApiDatabaseServiceJpa(
    private val checkRepository: CheckRepository,
) : CheckApiDatabaseService {
    override fun saveCheck(check: CheckResponse?): CheckResponse {
        if (check == null) {
            throw RuntimeException("Check is null")
        }

        val entity = CheckApiDtoConverter.fromDto(check.data.json, check.request.qrraw)
        checkRepository.save(entity)

        return check
    }

    override fun findCheckByQrRaw(qrRaw: String) : CheckDataEntity? {
        var result: CheckDataEntity? = checkRepository.findCheckByQrRaw(qrRaw.lowercase())
        return result
    }

}