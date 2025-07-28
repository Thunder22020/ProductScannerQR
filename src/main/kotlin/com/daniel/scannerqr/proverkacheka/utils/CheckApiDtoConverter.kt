package com.daniel.scannerqr.proverkacheka.utils

import com.daniel.scannerqr.proverkacheka.models.CheckResponse
import com.daniel.scannerqr.proverkacheka.models.DataWrapper
import com.daniel.scannerqr.proverkacheka.models.RequestWrapper
import com.daniel.scannerqr.proverkacheka.models.dto.CheckDataDTO
import com.daniel.scannerqr.proverkacheka.models.dto.ItemDTO
import com.daniel.scannerqr.proverkacheka.models.entities.CheckDataEntity
import com.daniel.scannerqr.proverkacheka.models.entities.ItemEntity

object CheckApiDtoConverter {

    fun fromDto(dto: CheckDataDTO, qrRaw: String?): CheckDataEntity {
        val check = CheckDataEntity(
            id = null,
            user = dto.user,
            userInn = dto.userInn,
            operator = dto.operator,
            totalSum = dto.totalSum,
            qrRaw = qrRaw,
            items = mutableListOf()
        )

        val items = dto.items.map {
            ItemEntity(
                id = null,
                name = it.name,
                price = it.price,
                quantity = it.quantity,
                check = check
            )
        }

        check.items = items.toMutableList()
        return check
    }

    fun toDto(entity: CheckDataEntity): CheckDataDTO {
        val items = entity.items.map {
            ItemDTO(
                id = it.id,
                name = it.name,
                price = it.price,
                quantity = it.quantity,
            )
        }

        return CheckDataDTO(
            items = items,
            totalSum = entity.totalSum,
            user = entity.user,
            userInn = entity.userInn,
            operator = entity.operator,
        )
    }

    fun toCheckResponse(entity: CheckDataEntity): CheckResponse {
        return CheckResponse(
            code = 1,
            first = 0,
            data = DataWrapper(
                json = toDto(entity),
            ),
            request = RequestWrapper(
                qrraw = entity.qrRaw
            )
        )
    }

}
