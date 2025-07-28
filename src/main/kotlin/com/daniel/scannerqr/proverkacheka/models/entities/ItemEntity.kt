package com.daniel.scannerqr.proverkacheka.models.entities

import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table(name = "items")
data class ItemEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @ManyToOne
    @JoinColumn(
        name = "check_id",
        referencedColumnName = "id"
    )
    var check: CheckDataEntity,

    val price: Double,
    val name: String,
    val quantity: Int,
)