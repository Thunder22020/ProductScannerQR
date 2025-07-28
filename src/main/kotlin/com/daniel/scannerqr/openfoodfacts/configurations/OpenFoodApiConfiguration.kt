package com.daniel.scannerqr.openfoodfacts.configurations

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.web.client.RestClient

@Configuration
class OpenFoodApiConfiguration {
    @Bean
    @Primary
    fun restClient(): RestClient {
        return RestClient.builder()
            .baseUrl("")
            .build()
    }
}