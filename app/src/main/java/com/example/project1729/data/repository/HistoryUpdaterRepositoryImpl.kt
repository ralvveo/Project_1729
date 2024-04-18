package com.example.project1729.data.repository

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.example.project1729.domain.model.Measurement
import com.example.project1729.domain.repository.HistoryUpdaterRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class HistoryUpdaterRepositoryImpl(private val context: Context) : HistoryUpdaterRepository {

    private val sharedPrefs = context.getSharedPreferences(PROJECT_1729_PREFERENCES, AppCompatActivity.MODE_PRIVATE)

    override fun readHistory(): List<Measurement> {
        val json = sharedPrefs!!.getString(HISTORY_LIST, null) ?: return listOf<Measurement>()
        val mutableListMeasurementType = object : TypeToken<List<Measurement>>() {}.type
        return Gson().fromJson(json, mutableListMeasurementType)
    }

    override fun clearHistory() {
        sharedPrefs!!.edit()
            .remove(HISTORY_LIST)
            .apply()
    }

    override fun addToHistory(measurement: Measurement){
        val trackList = readHistory().toMutableList()
        trackList.add(measurement)
        writeToHistory(trackList.toList())
    }

    override fun writeToHistory(historyList: List<Measurement>){
        val json = Gson().toJson(historyList)
        sharedPrefs!!.edit()
            .putString(HISTORY_LIST, json)
            .apply()
    }


    companion object{
        const val PROJECT_1729_PREFERENCES = "project_1729_preferences"
        const val HISTORY_LIST = "history_list"
    }

}