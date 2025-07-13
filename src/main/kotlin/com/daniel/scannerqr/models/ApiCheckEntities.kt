package com.daniel.scannerqr.models

import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.persistence.Entity
import jakarta.persistence.Column
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.JoinColumn
import jakarta.persistence.CascadeType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType

@Entity
@Table(name = "checks")
data class CheckData(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @OneToMany(mappedBy = "check")
    val items: List<Item>,

    @Column(name = "user_name")
    val user: String?,
    val userInn: String?,
    val operator: String?,
    val totalSum: Double?
)

@Entity
@Table(name = "items")
data class Item(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @ManyToOne(cascade = [(CascadeType.PERSIST)])
    @JoinColumn(
        name = "check_id",
        referencedColumnName = "id"
    )
    val check: CheckData,

    val price: Double,
    val name: String,
    val quantity: Int,
)