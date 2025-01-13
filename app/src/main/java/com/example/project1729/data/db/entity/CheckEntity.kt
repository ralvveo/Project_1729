package com.example.project1729.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "check_table")
data class CheckEntity (
    @PrimaryKey(autoGenerate = true)
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
