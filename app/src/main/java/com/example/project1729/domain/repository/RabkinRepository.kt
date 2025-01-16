package com.example.project1729.domain.repository

import com.example.project1729.domain.model.RabkinTest

interface RabkinRepository {
    fun getRandomRabkinTest(): RabkinTest
    fun doAnswer(rabkinTest: RabkinTest, userAnswer: String): Boolean
}