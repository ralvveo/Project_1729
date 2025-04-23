package com.example.project1729.ui.view_model


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project1729.domain.model.Test
import com.example.project1729.domain.repository.HistoryContentUpdaterRepository
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class HistoryContentViewModel : ViewModel(), KoinComponent {

    private val historyContentListLiveData = MutableLiveData<MutableList<Test>>()
    private val historyContentUpdater: HistoryContentUpdaterRepository by inject()
    fun getHistoryContentLiveData(): LiveData<MutableList<Test>> = historyContentListLiveData

    fun deleteTests(type: String){
        viewModelScope.launch {
            historyContentUpdater.deleteTests(type)
        }

    }
    init{
        checkData()
    }

    fun checkData(){
        viewModelScope.launch {
            historyContentListLiveData.value = historyContentUpdater.readHistory().toMutableList()
        }
    }



}