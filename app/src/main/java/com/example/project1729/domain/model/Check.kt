package com.example.project1729.domain.model

data class Check (
    val checkId: Long,
    val KCHSM: String,
    val dateAndTime: String,
    val eye: String,
    val measurementMethod: String,
    val diodeSaturation: String,
    val diodeSize: String,
    val diodeColor: String,
    val roomBrightness: String,
    val roomPressure: String,
    val roomTemperature: String,
    val roomHumidity: String,
    val eyeLoading: String,
    val eyeLoadingDuration: String,
    val eyeTraining: String

)