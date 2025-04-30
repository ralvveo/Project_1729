package com.example.project1729.data.model

import com.google.gson.JsonObject

data class LoginResponse(
    val accessToken: String,
    val refreshToken: String,
    val user: JsonObject,
)
