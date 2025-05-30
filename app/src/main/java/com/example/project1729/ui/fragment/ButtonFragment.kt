package com.example.project1729.ui.fragment

import android.app.Application
import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.project1729.R
import com.example.project1729.data.repository.NetworkRepositoryImpl.Companion.PROJECT1729_PREFERENCES
import com.example.project1729.databinding.FragmentButtonBinding
import com.example.project1729.domain.model.KchsmMeasure
import com.example.project1729.ui.view_model.KchsmViewModel
import com.example.project1729.voice.VoiceAssistant
import com.example.project1729.voice.VoiceAssistantManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.getValue

class ButtonFragment : Fragment(), VoiceAssistant.VoiceCallback {

    private val viewModel by viewModel<KchsmViewModel>()
    private lateinit var binding: FragmentButtonBinding
    private lateinit var voiceAssistant: VoiceAssistant
    private val prefs by lazy {
        requireContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    }
    private val COMMANDS_TOAST_SHOWN_KEY_BUTTON_FRAGMENT = "commands_toast_shown_button_fragment"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentButtonBinding.inflate(inflater, container, false)
        voiceAssistant = VoiceAssistant(requireContext(), this)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
    }

    private fun setupUI() {
        lifecycleScope.launch {
            delay(5000L)
            render()
        }

        binding.bigRoundButton.setOnClickListener {
            stopTestAndNavigateBack()
        }

        // Кнопка голосового управления
        binding.voiceButton.setOnClickListener {
            if (VoiceAssistantManager.isRecording()) {
                VoiceAssistantManager.stop()
            } else {
                if (checkAudioPermission()) {
                    VoiceAssistantManager.start()
                }
            }
            updateButtonState()
        }

        binding.voiceButton.setOnLongClickListener {
            showCommandsToast()
            true
        }
    }

    private fun stopTestAndNavigateBack() {
        viewModel.sendMessage("STOP")
        lifecycleScope.launch {
            delay(1000L)
            findNavController().navigateUp()
        }
    }

    private fun render() {
        val sharedPrefs = requireActivity().getSharedPreferences(
            PROJECT1729_PREFERENCES,
            Application.MODE_PRIVATE
        )
        val temperature = truncateAfterDot(sharedPrefs.getString("temperature", "--")?: "24")
        val humidity = truncateAfterDot(sharedPrefs.getString("humidity", "--") ?: "41")
        val lux = truncateAfterDot(sharedPrefs.getString("lux", "--") ?: "74")
        binding.temperatureValue.text = "$temperature °C"
        binding.humidityValue.text = "$humidity %"
        binding.luxValue.text = "$lux лк"
    }

    private fun truncateAfterDot(input: String): String {
        val dotIndex = input.indexOf('.')
        return if (dotIndex != -1) {
            input.substring(0, dotIndex)
        } else {
            input
        }
    }

    // Голосовые команды
    override fun onVoiceCommandRecognized(command: String) {
        activity?.runOnUiThread {
            when (command) {
                "завершить" -> {
                    stopTestAndNavigateBack()
                    Toast.makeText(context, "Тест завершен", Toast.LENGTH_SHORT).show()
                }
                "команды" -> showCommandsToast()
                else -> Toast.makeText(context, "Команда '$command' не поддерживается", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onVoiceTextRecognized(text: String, type: String) {
        activity?.runOnUiThread {
            when (type) {
                "command" -> onVoiceCommandRecognized(text)
                else -> Toast.makeText(context, "Распознано: $text", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onError(error: String) {
        activity?.runOnUiThread {
            Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
        }
    }

    // Вспомогательные методы
    private fun checkAudioPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun updateButtonState() {
        binding.voiceButton.setImageResource(
            if (VoiceAssistantManager.isRecording()) R.drawable.ic_mic_on
            else R.drawable.audio_mic_off_24
        )
    }

    private val availableCommands = listOf(
        "завершить" to "Завершить тест и вернуться",
        "команды" to "Показать список команд"
    )

    private fun showCommandsToast() {
        val commandsText = availableCommands.joinToString("\n") {
            "• ${it.first} - ${it.second}"
        }

        val toast = Toast(context).apply {
            duration = Toast.LENGTH_LONG
            view = layoutInflater.inflate(R.layout.toast_wide_layout, null).apply {
                findViewById<TextView>(R.id.toast_text).text = "Доступные команды:\n$commandsText"
            }
        }

        toast.setGravity(Gravity.TOP or Gravity.FILL_HORIZONTAL, 0, 32.toPx())
        toast.show()
    }

    private fun Int.toPx(): Int = (this * resources.displayMetrics.density).toInt()

    override fun onResume() {
        super.onResume()
        showCommandsToastIfNeeded()
        VoiceAssistantManager.registerCallback(this)
        updateButtonState()
    }

    private fun showCommandsToastIfNeeded() {
        if (!prefs.getBoolean(COMMANDS_TOAST_SHOWN_KEY_BUTTON_FRAGMENT, false)) {
            showCommandsToast()
            prefs.edit().putBoolean(COMMANDS_TOAST_SHOWN_KEY_BUTTON_FRAGMENT, true).apply()
        }
    }

    override fun onPause() {
        super.onPause()
        VoiceAssistantManager.unregisterCallback()
    }

    override fun onMessage(message: String) {
        activity?.runOnUiThread {
            Toast.makeText(context, "Говорите громче!", Toast.LENGTH_SHORT).show()
        }
    }
}