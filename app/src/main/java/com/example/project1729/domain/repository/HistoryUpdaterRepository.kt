package com.example.project1729.domain.repository

import com.example.project1729.domain.model.Measurement

interface HistoryUpdaterRepository {
    fun readHistory(): List<Measurement>
    fun writeToHistory(historyList: List<Measurement>)
    fun addToHistory(measurement: Measurement)
    fun clearHistory()
}