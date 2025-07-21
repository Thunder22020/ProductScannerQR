package com.daniel.scannerqr.proverkacheka.models.dto

data class CheckDataDTO(
    val items: List<ItemDTO>,
    val user: String?,
    val userInn: String?,
    val operator: String?,
    val totalSum: Double?
)