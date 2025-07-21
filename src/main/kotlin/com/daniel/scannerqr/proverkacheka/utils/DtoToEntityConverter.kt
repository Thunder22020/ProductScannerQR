package com.daniel.scannerqr.proverkacheka.utils

import com.daniel.scannerqr.proverkacheka.models.dto.CheckDataDTO
import com.daniel.scannerqr.proverkacheka.models.entities.CheckDataEntity
import com.daniel.scannerqr.proverkacheka.models.entities.ItemEntity

object DtoToEntityConverter {

    fun fromDto(dto: CheckDataDTO): CheckDataEntity {
        val check = CheckDataEntity(
            id = null,
            items = emptyList(),
            user = dto.user,
            userInn = dto.userInn,
            operator = dto.operator,
            totalSum = dto.totalSum
        )

        val items = dto.items.map { itemDto ->
            ItemEntity(
                id = null,
                name = itemDto.name,
                price = itemDto.price,
                quantity = itemDto.quantity,
                check = check
            )
        }

        return check.copy(items = items)
    }
}
