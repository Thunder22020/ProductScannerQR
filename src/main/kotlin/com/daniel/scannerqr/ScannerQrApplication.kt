package com.daniel.scannerqr

import org.springframework.boot.runApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class ScannerQrApplication

fun main(args: Array<String>) {
    runApplication<ScannerQrApplication>(*args)
}
