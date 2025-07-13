package com.daniel.scannerqr.repositories

import com.daniel.scannerqr.models.CheckData
import org.springframework.data.jpa.repository.JpaRepository

interface CheckRepository : JpaRepository<CheckData, Long>