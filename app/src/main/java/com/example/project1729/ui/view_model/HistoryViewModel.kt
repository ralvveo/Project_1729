package com.example.project1729.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.project1729.domain.model.Measurement
import com.example.project1729.domain.repository.HistoryUpdaterRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class HistoryViewModel : ViewModel(), KoinComponent {

    private val historyListLiveData = MutableLiveData<MutableList<Measurement>>()
    private val historyUpdater: HistoryUpdaterRepository by inject()
    fun getHistoryLiveData(): LiveData<MutableList<Measurement>> = historyListLiveData
    private fun getHistoryList(): List<Measurement>{
        return historyListLiveData.value ?: listOf()
    }

    init{
        historyListLiveData.value = historyUpdater.readHistory().toMutableList()
        writeToHistory(getHistoryList())
        historyListLiveData.postValue(historyListLiveData.value)
    }

    fun clearHistory(){
        historyUpdater.clearHistory()
        historyListLiveData.value = historyUpdater.readHistory().toMutableList()
    }

    fun writeToHistory(historyList: List<Measurement>){
        historyUpdater.writeToHistory(historyList)
    }

}