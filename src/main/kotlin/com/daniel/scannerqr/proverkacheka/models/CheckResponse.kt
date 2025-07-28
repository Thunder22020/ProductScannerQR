package com.daniel.scannerqr.proverkacheka.models

data class CheckResponse(
    val code: Int,
    val first: Int,
    val data: DataWrapper,
    val request: RequestWrapper
)