package com.example.project1729.domain.repository

import com.example.project1729.domain.model.Test

interface HistoryContentUpdaterRepository {

    suspend fun readHistory(): List<Test>
    suspend fun clearHistory()
    suspend fun deleteTests(type: String)
}