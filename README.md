# Spring Reactive Kotlin Example

A basic demo of a Kotlin microservice using reactive programming with Spring Boot, Spring WebFlux and DynamoDB.

- To run a DynamoDB instance locally:
```
docker-compose up -d
```
- To create the table used in this example:
```
./create-table.sh
```
- To run the project, simply run the main function on `SpringReactiveKotlinExampleApplication.kt`

Credits: https://medium.com/swlh/how-to-build-a-reactive-microservice-api-with-spring-boot-spring-webflux-and-dynamodb-using-kotlin-e1be3e99b15e