package com.example.project1729.domain.model

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable


@Serializable
data class UserRegister(
    @SerializedName("fio")
    val fio: String,
    @SerializedName("year")
    val year: String,
    @SerializedName("diagnoz")
    val diagnoz: String,
    @SerializedName("login")
    val login: String,
    @SerializedName("password")
    val password: String
)
