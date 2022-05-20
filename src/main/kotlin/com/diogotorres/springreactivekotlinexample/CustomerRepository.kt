package com.diogotorres.springreactivekotlinexample

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient
import software.amazon.awssdk.services.dynamodb.model.AttributeValue
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest
import software.amazon.awssdk.services.dynamodb.model.PutItemResponse

@Repository
class CustomerRepository(
    private val client: DynamoDbAsyncClient,
    @Value("\${application.dynamo.customer-table-name}")
    private val customerTableName: String
) {

    private val logger = LoggerFactory.getLogger(javaClass)

    fun saveCustomer(customer: CustomerPersist): Mono<PutItemResponse> {
        val putItemRequest = PutItemRequest.builder()
            .item(
                mapOf(
                    "id" to AttributeValue.builder().s(customer.id).build(),
                    "email" to AttributeValue.builder().s(customer.email).build(),
                    "firstName" to AttributeValue.builder().s(customer.firstName).build(),
                    "lastName" to AttributeValue.builder().s(customer.lastName).build()
                )
            )
            .tableName(customerTableName)
            .build()

        return Mono.fromFuture(client.putItem(putItemRequest))
            .doOnError { logger.error("Exception while saving Customer information - $it") }
    }

    fun retrieveCustomer(id: String): Mono<CustomerPersist> {
        val getItemRequest = GetItemRequest.builder()
            .key(
                mapOf(
                    "id" to AttributeValue.builder().s(id).build()
                )
            )
            .tableName(customerTableName)
            .build()

        return Mono.fromFuture(client.getItem(getItemRequest))
            .map {
                CustomerPersist(
                    id,
                    (it.item()["email"] ?: error("email N/A")).s(),
                    (it.item()["firstName"] ?: error("firstName N/A")).s(),
                    (it.item()["lastName"] ?: error("lastName N/A")).s()
                )
            }
            .doOnError { logger.error("Exception while retrieving Customer information - $it") }
    }
}