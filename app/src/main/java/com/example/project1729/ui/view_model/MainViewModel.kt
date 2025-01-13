package com.example.project1729.ui.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.project1729.App
import com.example.project1729.App.Companion.bluetoothController
import com.example.project1729.App.Companion.inputMessages
import com.example.project1729.App.Companion.resultKCHSM
import com.example.project1729.App.Companion.resultPressure
import com.example.project1729.App.Companion.resultTemp
import com.example.project1729.bt.bluetooth.BluetoothController
import com.example.project1729.creator.Creator
import com.example.project1729.domain.model.Measurement
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.random.Random

class MainViewModel : ViewModel(), BluetoothController.Listener  {

    private var mes1 = "1"
    private var mes2 = "1"
    private var mes3 = "1"

    private val state = MutableLiveData<MutableMap<String, String>>()
    private val historyUpdater = Creator.provideHistoryUpdater()


    init{
        state.value = DEFAULT_STATE
    }

    fun getState(): LiveData<MutableMap<String, String>> = state


    fun connect(){
        if (!App.bluetoothConnected) {
            bluetoothController.connect(this)
            //App.bluetoothConnected = true

        }
        else {
            bluetoothController.closeConnection()
            App.bluetoothConnected = false
        }
        changeConnectionState(App.bluetoothConnected)
    }
    fun testingEyeButtonPressed(btnNum : Int){

        when(btnNum){
            1 -> {
                state.value?.set(EYE, LEFT)
                bluetoothController.sendMessage("l")

            }
            2 -> {
                state.value?.set(EYE, RIGHT)
                bluetoothController.sendMessage("p")
            }

        }
        state.postValue(state.value)
    }

    fun measurementMethodButtonPressed(btnNum : Int){
        when (btnNum){
            1 -> {
                state.value?.set(METHOD, AUTO)
                bluetoothController.sendMessage("ma")
            }
            2 -> {
                state.value?.set(METHOD, STRAIGHT)
                bluetoothController.sendMessage("ms")
            }

            3 -> {
                state.value?.set(METHOD, REVERSED)
                bluetoothController.sendMessage("mr")
            }

            4 -> {
                state.value?.set(METHOD, OTHER)
                bluetoothController.sendMessage("mo")
            }
        }
        state.postValue(state.value)
    }

    fun radiationIntensityButtonPressed(btnNum : Int){
        when (btnNum){
            1 -> {
                state.value?.set(INTENSITY, AUTO)
                bluetoothController.sendMessage("qa")
            }
            2 -> {
                state.value?.set(INTENSITY, LOW)
                bluetoothController.sendMessage("q20")
            }
            3 -> {
                state.value?.set(INTENSITY, MIDDLE)
                bluetoothController.sendMessage("q50")
            }
            4 -> {
                state.value?.set(INTENSITY, HIGH)
                bluetoothController.sendMessage("q80")
            }
        }
        state.postValue(state.value)
    }

    fun diodeColorChanged(btnNum: Int){
        when (btnNum){
            1 -> {
                state.value?.set(DIODECOLOR, RED)
                bluetoothController.sendMessage("r")
            }
            2 -> {
                state.value?.set(DIODECOLOR, GREEN)
                bluetoothController.sendMessage("g")
            }
            3 -> {
                state.value?.set(DIODECOLOR, BLUE)
                bluetoothController.sendMessage("b")
            }
            4 -> {
                state.value?.set(DIODECOLOR, WHITE)
                bluetoothController.sendMessage("w")
            }
        }
        state.postValue(state.value)
    }

    fun setSeekBarProgress(progress: Int) {
        when (progress){
            in 0..33 -> state.value?.set(INTENSITY, LOW)
            in 34..66 -> state.value?.set(INTENSITY, MIDDLE)
            in 67..100 -> state.value?.set(INTENSITY, HIGH)
        }
        state.value?.set(INTENSITYSLIDER, progress.toString())
        bluetoothController.sendMessage("q+$progress")
        state.postValue(state.value)
    }

    fun changeConnectionState(connected: Boolean){
        state.value?.set(BLUETOOTH, when(connected){
            true -> CONNECTED
            false -> NOT_CONNECTED
        })
        state.postValue(state.value)
    }
    private fun measureGenerator(): Measurement{
        val currentDateAndTime = System.currentTimeMillis()
        val formatedDateAndTime = SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.getDefault()).format(currentDateAndTime)
        val colorList = listOf("yellow", "pinky", "blue", "purple")
        val deviceList = listOf("98:D3:31:FB:12:A9", "84:D4:45:FG:11:H7", "54:D1:76:DV:09:E1")
        val eyeList = listOf("Правый", "Левый")
        val measurementMethodList = listOf("Прямой", "Обратный", "Другой")
        val diodeColorLst = listOf("Красный", "Зелёный", "Синий", "Белый")
        return Measurement(
            dateAndTime = formatedDateAndTime,
            device = deviceList.random(),
            eye = getState().value?.get(EYE) ?: LEFT,
            measurementMethod = getState().value?.get(METHOD) ?: STRAIGHT,
            diodeColor = getState().value?.get(DIODECOLOR) ?: RED,
            KCHSM = getState().value?.get(KCHSM) ?: "0Гц",
            backgroundColor = colorList.random()
            )

//        KCHSM = Random.nextInt(40,200).toString(),
    }
    fun startMeasure(){
        bluetoothController.sendMessage("a")
        repeat(10){Log.d("SENDA", "SENDA")}
        val test = measureGenerator()
        historyUpdater.addToHistory(test)
    }
    private var resultMessageTemp = ""
    private var resultMessagePressure = ""
    private var resultMessageKCHSM = ""
    private var stepFlagTemp = 0
    private var stepFlagPressure = 0
    private var stepFlagKCHSM = 0
    private var whatFlag:String = ""

    override fun onReceive(message: String) {
        when (message) {
            "bluetooth connected" -> {
                App.bluetoothConnected = true
                changeConnectionState(App.bluetoothConnected)
                bluetoothController.sendMessage("+")
            }

            "bluetooth no connected" -> {
                App.bluetoothConnected = false
                changeConnectionState(App.bluetoothConnected)
            }

            else -> {
                inputMessages += "message"
                analyzeMessageChange()
            }
        }
    }


    private fun analyzeMessageChange(){
        var currentTemp = ""
        var currentPressure = ""
        var currentKCHSM = ""
        val currentMessagesLength = inputMessages.length
        for (i in 0..<currentMessagesLength){
            when (inputMessages[i]){
                't' -> {
                    if (currentMessagesLength - i >= 3){
                        currentTemp = "${inputMessages[i+1]}${inputMessages[i+2]}"
                    }
                }
                'p' -> {
                    if (currentMessagesLength - i >= 3){
                        currentPressure = "${inputMessages[i+1]}${inputMessages[i+2]}${inputMessages[i+3]}${inputMessages[i+4]}"
                    }
                }

                'k' -> {
                    if (currentMessagesLength - i >= 3){
                        currentKCHSM = "${inputMessages[i+1]}${inputMessages[i+2]}"
                    }
                }
            }
        }

        if (currentTemp != resultTemp){
            resultTemp = currentTemp
            state?.value?.set(TEMPERATURE, "$resultMessageTemp°С") ?: "20°С"
            repeat(10) { Log.d("SETTEMP", "$resultMessageTemp") }
            state.postValue(state.value)
        }
        if (currentPressure != resultPressure){
            resultPressure = currentPressure
            state?.value?.set(PRESSURE, "${resultMessagePressure}кПа") ?: "100кПа"
            repeat(10) { Log.d("SETPRESSURE", "$resultMessagePressure") }
            state.postValue(state.value)
        }
        if (currentKCHSM != resultKCHSM){
            resultKCHSM = currentKCHSM
            state?.value?.set(KCHSM, "${resultMessageKCHSM}Гц") ?: "0Гц"
            repeat(10) { Log.d("SETKCHSM", "$resultMessageKCHSM") }
            state.postValue(state.value)
        }



    }
//                when (message[0]) {
//
//                    't' -> {
//                        whatFlag = "TEMP"
//                    }
//
//                    'p' -> {
//                        whatFlag = "PRESSURE"
//                    }
//
//                    'k' -> {
//                        whatFlag = "KCHSM"
//                        repeat(10){
//                            Log.d("KCHSM", "KCHSM")
//                        }
//                    }
//                }

//                when (whatFlag) {
//                    "TEMP" -> {
//                        when (stepFlagTemp){
//                            0 -> {
//                                resultMessageTemp = ""
//                                stepFlagTemp = 1
//                            }
//
//                            1 -> {
//                                if (message != "t") {
//                                    resultMessageTemp = message
//
//                                }
//                                stepFlagTemp = 2
//                            }
//
//                            2 -> {
//                                if (message != "t") {
//
//                                    resultMessageTemp += message
//                                    state?.value?.set(TEMPERATURE, "$resultMessageTemp°С") ?: "100"
//                                    repeat(10) { Log.d("SETTEMP", "$resultMessageTemp") }
//
//                                    state.postValue(state.value)
//                                    resultMessageTemp = ""
//                                }
//                                stepFlagTemp = 0
//                            }
//
//                        }
//
//                    }
//
//                    "PRESSURE" -> {
//                        when (stepFlagPressure){
//                            0 -> {
//                                resultMessagePressure= ""
//                                stepFlagPressure = 1
//                            }
//
//                            1 -> {
//                                if (message != "p") {
//                                    resultMessagePressure = message
//
//                                }
//                                stepFlagPressure = 2
//                            }
//
//                            2 -> {
//                                if (message != "p") {
//                                    resultMessagePressure += message
//                                    state?.value?.set(PRESSURE, "${resultMessagePressure}кПа") ?: "100"
//                                    repeat(10) { Log.d("SETPRESSURE", "$resultMessagePressure") }
//                                    state.postValue(state.value)
//                                    resultMessagePressure = ""
//                                }
//                                stepFlagPressure = 0
//                            }
//
//                        }
//
//                    }
//
//
//
//
//                    "KCHSM" -> {
//                        when (stepFlagKCHSM){
//                            0 -> {
//                                resultMessageKCHSM= ""
//                                stepFlagKCHSM = 1
//                            }
//
//                            1 -> {
//                                if (message != "k") {
//                                    resultMessageKCHSM = message
//
//                                }
//                                stepFlagKCHSM = 2
//                            }
//
//                            2 -> {
//                                if (message != "k") {
//                                    resultMessageKCHSM += message
//                                    state?.value?.set(KCHSM, "${resultMessageKCHSM}Гц") ?: "0"
//                                    repeat(10) { Log.d("SETKCHSM", "$resultMessageKCHSM") }
//                                    state.postValue(state.value)
//                                    resultMessageKCHSM = ""
//                                }
//                                stepFlagKCHSM = 0
//                            }
//
//                        }
//
//                    }
//
//
//
//
//                    else -> {}
//                }







                //{binding.tvStatus2.text = message}


    companion object {
        fun factory(): ViewModelProvider.Factory {
            return viewModelFactory {
                initializer {
                    MainViewModel()
                }
            }
        }
        const val BLUETOOTH = "bluetooth"
        const val EYE = "eye"
        const val METHOD = "method"
        const val INTENSITYSLIDER = "intensitySlider"
        const val INTENSITY = "intensity"
        const val DIODECOLOR = "diodeColor"
        const val KCHSM = "KCHSM"
        const val PRESSURE = "pressure"
        const val TEMPERATURE = "temperature"

        const val CONNECTED = "connected"
        const val NOT_CONNECTED = "notConnected"

        const val LEFT = "left"
        const val RIGHT = "right"

        const val AUTO = "auto"
        const val STRAIGHT = "straight"
        const val REVERSED = "reversed"
        const val OTHER = "other"

        const val LOW = "low"
        const val MIDDLE = "middle"
        const val HIGH = "high"

        const val RED = "red"
        const val GREEN = "green"
        const val BLUE = "blue"
        const val WHITE = "white"


        val DEFAULT_STATE: MutableMap<String, String> = mutableMapOf(
            BLUETOOTH to NOT_CONNECTED,
            EYE to LEFT,
            METHOD to STRAIGHT,
            INTENSITYSLIDER to "0",
            INTENSITY to AUTO,
            DIODECOLOR to RED,
            KCHSM to "--Гц",
            PRESSURE to "--кПа",
            TEMPERATURE to "--°С"
        )
    }

}