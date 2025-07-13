package com.daniel.scannerqr.repositories

import com.daniel.scannerqr.models.Item
import org.springframework.data.jpa.repository.JpaRepository

interface ItemRepository : JpaRepository<Item, Long>