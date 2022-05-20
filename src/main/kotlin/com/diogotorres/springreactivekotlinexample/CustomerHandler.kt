package com.diogotorres.springreactivekotlinexample

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.json
import reactor.core.publisher.Mono
import java.util.UUID

@Component
class CustomerHandler(
    private val customerRepository: CustomerRepository
) {

    private val logger = LoggerFactory.getLogger(javaClass)

    fun saveCustomerInformation(request: ServerRequest): Mono<ServerResponse> {
        val customerId = generateCustomerId()
        return request.bodyToMono(Customer::class.java)
            .map { CustomerPersist(customerId, it.email, it.firstName, it.lastName) }
            .flatMap { customerRepository.saveCustomer(it) }
            .flatMap { ServerResponse.ok().json().bodyValue("{\"id\": \"$customerId\"}") }
            .doOnError { logger.error("Exception while trying to store a new customer record - $it") }
    }

    fun retrieveCustomerInformation(request: ServerRequest): Mono<ServerResponse> {
        return Mono.fromSupplier { request.pathVariable("id") }
            .flatMap { customerRepository.retrieveCustomer(it) }
            .flatMap { ServerResponse.ok().json().body(Mono.just(it), CustomerPersist::class.java) }
            .doOnError { logger.error("Exception while trying to retrieve a customer record - $it") }
    }

    private fun generateCustomerId() = UUID.randomUUID().toString()
}