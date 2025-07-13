package com.daniel.scannerqr.utils

import com.daniel.scannerqr.models.Item
import com.daniel.scannerqr.models.CheckData
import com.daniel.scannerqr.models.CheckDataDTO

object DtoToEntityConverter {

    fun fromDto(dto: CheckDataDTO): CheckData {
        val check = CheckData(
            id = null,
            items = emptyList(),
            user = dto.user,
            userInn = dto.userInn,
            operator = dto.operator,
            totalSum = dto.totalSum
        )

        val items = dto.items.map { itemDto ->
            Item(
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
