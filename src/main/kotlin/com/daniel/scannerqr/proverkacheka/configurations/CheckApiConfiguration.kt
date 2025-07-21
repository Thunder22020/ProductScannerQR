package com.daniel.scannerqr.proverkacheka.configurations

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestClient

@Configuration
class CheckApiConfiguration {
    @Bean
    fun restClient(): RestClient {
        return RestClient.builder()
            .baseUrl("https://proverkacheka.com/api/v1")
            .build()
    }
}