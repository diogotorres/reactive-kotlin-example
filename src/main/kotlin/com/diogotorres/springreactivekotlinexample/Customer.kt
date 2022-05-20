package com.diogotorres.springreactivekotlinexample

data class Customer(
    val email: String,
    val firstName: String,
    val lastName: String
)

data class CustomerPersist(
    val id: String,
    val email: String,
    val firstName: String,
    val lastName: String
)