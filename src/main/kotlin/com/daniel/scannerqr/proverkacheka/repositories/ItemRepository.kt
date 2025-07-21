package com.daniel.scannerqr.proverkacheka.repositories

import com.daniel.scannerqr.proverkacheka.models.entities.ItemEntity
import org.springframework.data.jpa.repository.JpaRepository

interface ItemRepository : JpaRepository<ItemEntity, Long>