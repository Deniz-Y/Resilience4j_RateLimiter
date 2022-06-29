package com.valensas.controller

import com.valensas.MetricService
import com.valensas.RateLimiterAction
import io.github.resilience4j.ratelimiter.RequestNotPermitted
import io.github.resilience4j.ratelimiter.annotation.RateLimiter
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate



@RestController
class TestController (private val metricService: MetricService){

    @GetMapping("test")
    @RateLimiter(name = "processService",fallbackMethod = "greetingFallBack")
    fun greeting(): ResponseEntity<*>? {
        return ResponseEntity.ok().body("Hello World: ")
    }


    fun greetingFallBack(exp: Throwable): ResponseEntity<*>? {
        val responseHeaders = HttpHeaders()
        responseHeaders["Retry-After"] = "1" //retry after one second
        metricService.incrementRequestAction("","",RateLimiterAction.Block)
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
            .headers(responseHeaders) //send retry header
            .body("Too many request - No further calls are accepted")
    }



    @PostMapping("test")
    fun submit(
        @RequestBody body: TestBody
    ): String {
        return "Hello"
    }
}

data class TestBody(
    val id: Int,
    val name: String,
    val age: LocalDate?
)









