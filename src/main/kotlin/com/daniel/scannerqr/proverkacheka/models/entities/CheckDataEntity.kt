package com.daniel.scannerqr.proverkacheka.models.entities

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table

@Entity
@Table(name = "checks")
data class CheckDataEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @OneToMany(mappedBy = "check")
    val items: List<ItemEntity>,

    @Column(name = "user_name")
    val user: String?,
    val userInn: String?,
    val operator: String?,
    val totalSum: Double?
)