package com.example.project1729.ui.view_model

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project1729.App
import com.example.project1729.App.Companion.bluetoothController
import com.example.project1729.bt.bluetooth.BluetoothController
import com.example.project1729.data.repository.NetworkRepositoryImpl.Companion.PROJECT1729_PREFERENCES
import com.example.project1729.domain.model.KchsmMeasure
import com.example.project1729.domain.model.KchsmMeasureData
import com.example.project1729.domain.model.KchsmMeaureResult
import com.example.project1729.domain.repository.RabkinRepository
import com.google.gson.Gson
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.getValue
import kotlin.random.Random

class KchsmViewModel(val context: Context) : ViewModel(), KoinComponent, BluetoothController.Listener{

    private val deviceLiveData = MutableLiveData<String>()
    private val resultLiveData = MutableLiveData<KchsmMeasure>()
    fun getDeviceLiveData(): LiveData<String> = deviceLiveData
    fun getResultLiveData(): LiveData<KchsmMeasure> = resultLiveData
    private val rabkinRepository: RabkinRepository by inject()

    init{
        viewModelScope.launch {
            delay(6000L)
            val result = Random.nextInt(20,80).toString()
            val value = KchsmMeasure(
                critical_frequency = result,
                temperature = "24.67",
                humidity = "35.56",
                lux = "100.71"
            )
            resultLiveData.postValue(value)
            rabkinRepository.saveMeasure("KCHSM", analyzeAndSaveMeasure(result))

        }

    }
    override fun onReceive(message: String) {
        if (message == "bluetooth connected"){
            App.bluetoothConnected = true
            deviceLiveData.postValue ("connected")
        }
        if (message[1] == 't'){
            val kchsmMeasureData = parseWithGson(message)
            val value = KchsmMeasure(
                critical_frequency = "--",
                temperature = kchsmMeasureData.temperature,
                humidity = kchsmMeasureData.humidity,
                lux = kchsmMeasureData.lux
            )
            resultLiveData.postValue(value)
            Log.d("RRRRT", kchsmMeasureData.toString())
            val sharedPrefs = context.getSharedPreferences(PROJECT1729_PREFERENCES, Application.MODE_PRIVATE)
            sharedPrefs.edit()
                .putString("temperature", kchsmMeasureData.temperature)
                .apply()
            sharedPrefs.edit()
                .putString("humidity", kchsmMeasureData.humidity)
                .apply()
            sharedPrefs.edit()
                .putString("lux", kchsmMeasureData.lux)
                .apply()
        }


        if (message[1] == 'c'){
            val kchsmMeasureResult = parseResultWithGson(message)
            resultLiveData.postValue(resultLiveData.value?.copy(critical_frequency = kchsmMeasureResult.critical_frequency))
            rabkinRepository.saveMeasure("kchsm", analyzeAndSaveMeasure(kchsmMeasureResult.critical_frequency))
        }
        Log.d ("RRRR", message )
    }

    fun sendMessage(message: String){
        bluetoothController.sendMessage(message = message)
    }

    fun connect() {
        bluetoothController.connect(this)
        App.bluetoothConnected = true //Comment this
        deviceLiveData.value = "connected" //Comment this
    }

    fun unconnect() {
        bluetoothController.closeConnection()
        App.bluetoothConnected = false
        deviceLiveData.value = "not_connected"
    }


    private fun parseWithGson(rawInput: String): KchsmMeasureData {
        // Преобразуем в валидный JSON: добавим кавычки
        val json = rawInput
            .replace("{", "{\"")
            .replace(":", "\":\"")
            .replace(",", "\",\"")
            .replace("}", "\"}")

        return Gson().fromJson(json, KchsmMeasureData::class.java)
    }

    private fun parseResultWithGson(rawInput: String): KchsmMeaureResult {
        val json = rawInput
            .replace("{", "{\"")
            .replace(":", "\":\"")
            .replace(",", "\",\"")
            .replace("}", "\"}")

        return Gson().fromJson(json, KchsmMeaureResult::class.java)
    }
    private fun analyzeAndSaveMeasure(result: String): String {
        if (result.toInt() <= 30){
            return "low"
        }
        else if ((result.toInt() > 30) and (result.toInt() <= 60)){
            return "ok"
        }
        else{
            return "high"
        }
    }
}