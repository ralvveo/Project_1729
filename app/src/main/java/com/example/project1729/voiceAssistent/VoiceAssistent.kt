package com.example.project1729.utils

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.project1729.R
import com.example.project1729.ui.fragment.RegisterFragment
import com.example.project1729.ui.fragment.StartFragment
import com.example.project1729.voiceAssistent.VoiceCommandHandler
import java.sql.DriverManager.println

class VoiceAssistant(private val context: Context, private val navController: NavController, private val commandHandler: VoiceCommandHandler) {

    private var speechRecognizer: SpeechRecognizer? = null
    private var isEnabled: Boolean = false


    fun startListening() {
        println("startListening")

        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ru-RU")
        }

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context)

        speechRecognizer?.setRecognitionListener(object : RecognitionListener {
            override fun onResults(results: Bundle) {
                val matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                if (matches != null && matches.isNotEmpty()) {
                    Log.d("VoiceAssistant", "$matches")
                    val command = matches[0].lowercase()
                    when {
                        command.contains("войти", ignoreCase = true) -> {
                            navController.navigate(R.id.action_startFragment_to_loginFragment)
                        }
                        command.contains("регистрация", ignoreCase = true) -> {
                            navController.navigate(R.id.action_startFragment_to_registerFragment)
                        }
                        command.contains("зарегистрироваться", ignoreCase = true) -> {
                            navController.navigateUp()
                        }
                        command.contains("Авторизоваться", ignoreCase = true) -> {
                            navController.navigate(R.id.action_loginFragment_to_checkFragment)
                        }
                        command.contains("Начать", ignoreCase = true) -> {
                            navController.navigate(R.id.action_checkFragment_to_dopInfoFragment)
                        }
                        command.contains("Старт", ignoreCase = true) -> {
                            navController.navigateUp()
                        }
                        command.contains("фио", ignoreCase = true) -> {
                            val fio = command.replace("заполнить фио", "").trim()
                            commandHandler.updateFioField(fio)
                        }
                        command.contains("год", ignoreCase = true) -> {
                            val year = command.replace("год", "").trim()
                            commandHandler.updateYearField(year)
                        }
                        command.contains("диагноз", ignoreCase = true) -> {
                            val diagnosis = command.replace("диагноз", "").trim()
                            commandHandler.updateDiagnosisField(diagnosis)
                        }
                        command.contains("логин", ignoreCase = true) -> {
                            val login = command.replace("логин ", "").trim()
                            commandHandler.updateLoginField(login)
                        }
                        command.contains("пароль", ignoreCase = true) -> {
                            val password = command.replace("пароль ", "").trim()
                            commandHandler.updatePasswordField(password)
                        }
                        else -> {
                            Toast.makeText(context, "Команда не распознана", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }

            override fun onPartialResults(p0: Bundle?) {
                val matches = p0!!.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                val recognizedText = matches?.get(0)
                println(matches.toString())
                println(recognizedText)
            }

            override fun onEvent(p0: Int, p1: Bundle?) {
                Log.d("VoiceAssistant", "event")
            }

            override fun onReadyForSpeech(params: Bundle) {
                Log.d("VoiceAssistant", "Ready for speech")
            }
            override fun onError(error: Int) {
                Log.e("VoiceAssistant", "Error occurred: $error")
            }
            override fun onBeginningOfSpeech() {
                Log.d("VoiceAssistant", "Begin of speech")
            }
            override fun onRmsChanged(p0: Float) {
                Log.d("VoiceAssistant", "rms")
            }

            override fun onBufferReceived(p0: ByteArray?) {
                Log.d("VoiceAssistant", "buffer")
            }

            override fun onEndOfSpeech() {
                Log.d("VoiceAssistant", "End of speech")
            }
        })

        speechRecognizer?.startListening(intent)
    }

    fun stopListening() {
        speechRecognizer?.stopListening()
        speechRecognizer?.cancel()
        println("stopListening")
    }

    fun isVoiceAssistantEnabled(): Boolean {
        return isEnabled
    }

    fun toggleVoiceAssistant() {
        isEnabled = !isEnabled
    }
}
