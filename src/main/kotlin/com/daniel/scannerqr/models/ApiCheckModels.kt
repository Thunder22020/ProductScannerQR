package com.daniel.scannerqr.models

data class CheckResponse(
    val code: Int,
    val first: Int,
    val data: DataWrapper
)

data class DataWrapper(
    val json: CheckDataDTO
)


data class CheckDataDTO(
    val items: List<ItemDTO>,
    val user: String?,
    val userInn: String?,
    val operator: String?,
    val totalSum: Double?
)


data class ItemDTO(
    var id: Long,
    val price: Double,
    val name: String,
    val quantity: Int,
)