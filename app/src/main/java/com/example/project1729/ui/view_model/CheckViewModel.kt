package com.example.project1729.ui.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project1729.data.repository.CheckSaverRepository
import com.example.project1729.domain.model.Check
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.random.Random

class CheckViewModel : ViewModel(), KoinComponent{

    private val checkSaverRepository: CheckSaverRepository by inject()
    fun startCheck(){

        viewModelScope.launch {
            val randomCheck = checkGenerator()
            checkSaverRepository.saveCheck(randomCheck)
        }
    }


    private fun checkGenerator(): Check {
        val currentDateAndTime = System.currentTimeMillis()
        val formatedDateAndTime = SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.getDefault()).format(currentDateAndTime)
        val eyeList = listOf("Правый", "Левый")
        val measurementMethodList = listOf("Прямой", "Обратный", "Другой")
        val diodeColorList = listOf("Красный", "Зелёный", "Синий", "Белый")
        val eyeLoadingList = listOf("Cмартфон","Телевизор","Компьютер","")
        val eyeTrainingList = listOf("Да", "Нет")
        return Check(
            checkId = 0,
            KCHSM = Random.nextInt(20,60).toString(),
            dateAndTime = formatedDateAndTime,
            eye = eyeList.random(),
            measurementMethod = measurementMethodList.random(),
            diodeSaturation = Random.nextInt(20, 90).toString(),
            diodeSize = "70",
            diodeColor = diodeColorList.random(),
            roomBrightness = Random.nextInt(20, 500).toString(),
            roomPressure = Random.nextInt(50, 200).toString(),
            roomTemperature = Random.nextInt(5, 30).toString(),
            roomHumidity = Random.nextInt(50, 100).toString(),
            eyeLoading = eyeLoadingList.random(),
            eyeLoadingDuration = Random.nextInt(10, 200).toString(),
            eyeTraining = eyeTrainingList.random()
        )

    }
}