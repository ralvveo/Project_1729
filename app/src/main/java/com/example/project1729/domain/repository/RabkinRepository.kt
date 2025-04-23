package com.example.project1729.domain.repository

import com.example.project1729.domain.model.RabkinTest
import com.example.project1729.domain.model.SivtsevTest

interface RabkinRepository {
    fun getRandomRabkinTest(): RabkinTest
    fun getRandomSivtsevTest(): SivtsevTest
    fun doAnswer(rabkinTest: RabkinTest, userAnswer: String): Boolean
    fun doAnswer(sivtsevTest: SivtsevTest, userAnswer: String): Boolean
    fun saveMeasure(type: String, result: String)
}