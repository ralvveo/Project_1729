package com.example.project1729.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "test_table")
data class TestEntity (
    @PrimaryKey(autoGenerate = true)
    val testId: Long,
    val type: String,
    val dateAndTime: String,
    val result: String

)

