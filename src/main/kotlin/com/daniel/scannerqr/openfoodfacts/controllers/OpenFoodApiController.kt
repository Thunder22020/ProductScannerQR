package com.daniel.scannerqr.openfoodfacts.controllers

import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.client.RestClient
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import reactor.core.publisher.Mono
import java.net.URI
import java.net.URLEncoder

@RestController
@RequestMapping("/api/openfood/")
class OpenFoodApiController {

    private val webClient = WebClient.builder()
        .baseUrl("https://world.openfoodfacts.org")  // 👈 ОБЯЗАТЕЛЬНО world, не ru
        .defaultHeader(HttpHeaders.USER_AGENT, "Mozilla/5.0")
        .defaultHeader(HttpHeaders.ACCEPT_ENCODING, "identity")  // 👈 отключаем gzip
        .build()

    @GetMapping("/{name}")
    fun getFoodInfo(@PathVariable name: String): Mono<ResponseEntity<String>> {
        val normalized = normalizeQuery(name)
        val encoded = URLEncoder.encode(normalized, "UTF-8")
        val uri = "/cgi/search.pl?search_terms=$encoded&search_simple=1&action=process&json=1"

        println("Request URI: $uri")

        return webClient.get()
            .uri(uri)
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .bodyToMono(String::class.java)
            .map { ResponseEntity.ok(it) }
            .onErrorResume { e -> Mono.just(ResponseEntity.status(500).body("Error: ${e.message}")) }
    }

    fun normalizeQuery(name: String): String {
        val stopWords = listOf(
            "\\d+г", "\\d+шт", "\\d+см",
            "пэт", "упаковка", "пакет", "майка", "2сл", "4рул", "штук", "фестив", "дарк", "волшебный", "цветок",
            "ооо", "зао", "оао", "ип", "плюс", "с", "и", "в", "на", "со", "для", "из", "от", "по", "без", "№"
        )

        return name.lowercase()
            .replace(Regex("[^а-яё0-9a-z ]"), " ")
            .replace(Regex("\\s+"), " ")
            .split(" ")
            .filter { word -> stopWords.none { sw -> word.matches(Regex(sw)) } && word.length > 2 }
            .joinToString(" ")
    }

    @GetMapping
    fun justGetInfo(): String? {
        val client = RestClient.builder()
            .baseUrl("https://world.openfoodfacts.org")
            .build()

        return client.get()
            .uri("/cgi/search.pl?search_terms=шоколад&search_simple=1&action=process&json=1")
            .header("User-Agent", "PostmanRuntime/7.44.1")
            .header("Accept", "*/*")
            .header("Accept-Encoding", "gzip, identity") // отключить gzip
            .retrieve()
            .body(String::class.java)
    }
}
