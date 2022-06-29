package com.valensas

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import java.lang.Thread.sleep

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class RateLimiterApplicationTests {
    @Autowired
    lateinit var restTemplate: TestRestTemplate

    private val logger = LoggerFactory.getLogger(javaClass)

    @Test
    fun contextLoads() {
        val limit = 1
        var intTime = 0
        (0..10).forEach {
            val response = restTemplate.exchange("/test", HttpMethod.GET, null, String::class.java)
            logger.info("Request: {} - Status: {}", it, response.statusCode)
            val expectedStatusCode = if (it < limit) HttpStatus.OK else HttpStatus.TOO_MANY_REQUESTS
            assertEquals(expectedStatusCode, response.statusCode)

        }
    }
}




