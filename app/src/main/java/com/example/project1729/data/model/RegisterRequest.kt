package com.example.project1729.data.model

data class RegisterRequest(
    val login: String,
    val password: String,
    val fio: String,
    val year: String,
    val diagnoz: String
)

