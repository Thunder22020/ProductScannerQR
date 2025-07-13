package com.daniel.scannerqr.configurations

import org.springframework.web.client.RestClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AppConfiguration {
    @Bean
    fun restClient(): RestClient {
        return RestClient.create()
    }
}