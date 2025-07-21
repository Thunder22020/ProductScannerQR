package com.daniel.scannerqr.proverkacheka.services.db

import com.daniel.scannerqr.proverkacheka.models.CheckResponse
import com.daniel.scannerqr.proverkacheka.repositories.ItemRepository
import com.daniel.scannerqr.proverkacheka.utils.DtoToEntityConverter
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
    private val itemRepository: ItemRepository,
) : CheckApiDatabaseService {
    override fun saveCheck(check: CheckResponse?): CheckResponse {
        if (check == null) {
            throw RuntimeException("Check is null")
        }

        val entity = DtoToEntityConverter.fromDto(check.data.json)
        itemRepository.saveAll(entity.items)
        return check
    }
}