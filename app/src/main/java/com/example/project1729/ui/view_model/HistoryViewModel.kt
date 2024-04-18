package com.example.project1729.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.project1729.creator.Creator
import com.example.project1729.domain.model.Measurement

class HistoryViewModel : ViewModel() {

    private val historyListLiveData = MutableLiveData<MutableList<Measurement>>()
    private val historyUpdater = Creator.provideHistoryUpdater()
    fun getHistoryLiveData(): LiveData<MutableList<Measurement>> = historyListLiveData
    private fun getHistoryList(): List<Measurement>{
        return historyListLiveData.value ?: listOf()
    }

    init{
//        historyListLiveData.value = mutableListOf()
//        historyListLiveData.value?.add(test1)
//        historyListLiveData.value?.add(test2)
//        historyListLiveData.value?.add(test3)
//        historyListLiveData.value?.add(test4)
//        historyListLiveData.value?.add(test5)
//        historyListLiveData.value?.add(test6)
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

    companion object {
        fun factory(): ViewModelProvider.Factory {
            return viewModelFactory {
                initializer {
                    HistoryViewModel()
                }
            }
        }


        val test1: Measurement = Measurement(
            dateAndTime = "18.02.2024 13:03",
            device = "98:D3:31:FB:12:A9",
            eye = "Правый",
            measurementMethod = "Прямой",
            diodeColor = "Красный",
            KCHSM = "145",
            backgroundColor = "blue"
        )

        val test2: Measurement = Measurement(
            dateAndTime = "02.02.2024 9:01",
            device = "98:D3:31:FB:12:A9",
            eye = "Правый",
            measurementMethod = "Обратный",
            diodeColor = "Синий",
            KCHSM = "205",
            backgroundColor = "yellow"
        )

        val test3: Measurement = Measurement(
            dateAndTime = "27.01.2024 21:04",
            device = "35:FC:5F:BF:BD:CB",
            eye = "Левый",
            measurementMethod = "Прямой",
            diodeColor = "Белый",
            KCHSM = "145",
            backgroundColor = "pinky"
        )

        val test4: Measurement = Measurement(
            dateAndTime = "27.01.2024 21:04",
            device = "35:FC:5F:BF:BD:CB",
            eye = "Левый",
            measurementMethod = "Прямой",
            diodeColor = "Белый",
            KCHSM = "145",
            backgroundColor = "purple"
        )

        val test5: Measurement = Measurement(
            dateAndTime = "27.01.2024 21:04",
            device = "35:FC:5F:BF:BD:CB",
            eye = "Левый",
            measurementMethod = "Прямой",
            diodeColor = "Белый",
            KCHSM = "145",
            backgroundColor = "blue"
        )

        val test6: Measurement = Measurement(
            dateAndTime = "27.01.2024 21:04",
            device = "35:FC:5F:BF:BD:CB",
            eye = "Левый",
            measurementMethod = "Прямой",
            diodeColor = "Белый",
            KCHSM = "145",
            backgroundColor = "yellow"
        )
    }
}