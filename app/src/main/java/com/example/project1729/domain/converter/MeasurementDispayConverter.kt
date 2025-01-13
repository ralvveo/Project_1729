package com.example.project1729.domain.converter

import com.example.project1729.domain.model.ConvertedMeasurement
import com.example.project1729.domain.model.Measurement
import com.example.project1729.ui.view_model.MainViewModel
import kotlin.random.Random

object MeasurementDispayConverter {
    fun convert(measurement: Measurement) : ConvertedMeasurement{
        return ConvertedMeasurement(
            dateAndTime = measurement.dateAndTime,
            device = measurement.device,
            eye = convertEye(measurement.eye),
            measurementMethod = convertMeasurementMethod(measurement.measurementMethod),
            diodeColor = convertDiodeColor(measurement.diodeColor),
            KCHSM = measurement.KCHSM,
            backgroundColor = measurement.backgroundColor
        )
    }

    private fun convertEye(eye: String): String{
        when (eye){
            "right" -> return "Правый"
            else -> return "Левый"
        }
    }

    private fun convertMeasurementMethod(method: String): String{
        when (method){
            "reversed" -> return "Обратный"
            "other" -> return "Другой"
            else -> return "Прямой"
        }
    }

    private fun convertDiodeColor(diodeColor: String): String{
        when (diodeColor){
            "green" -> return "Зелёный"
            "blue" -> return "Синий"
            "white" -> return "Белый"
            else -> return "Красный"
        }
    }



}
