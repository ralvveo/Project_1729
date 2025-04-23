package com.example.project1729.data.repository

import com.example.project1729.data.db.Database
import com.example.project1729.domain.converter.Converter
import com.example.project1729.domain.model.Test
import com.example.project1729.domain.repository.HistoryContentUpdaterRepository

class HistoryContentUpdaterRepositoryImpl(private val database: Database) : HistoryContentUpdaterRepository {
    override suspend fun readHistory(): List<Test> {
        return database.testDao().getTestsList().map{testEntity ->  Converter.convert(testEntity)}
    }

    override suspend fun clearHistory() {
        //TODOO
    }

    override suspend fun deleteTests(type: String) {
        database.testDao().deleteTests(type)
    }

}