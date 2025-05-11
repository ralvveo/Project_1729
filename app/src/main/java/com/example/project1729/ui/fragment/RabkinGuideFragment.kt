package com.example.project1729.ui.fragment

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.project1729.R
import com.example.project1729.data.keys.RABKIN_RESULTS
import com.example.project1729.data.keys.RABKIN_RESULTS.CURRENT_MEASURE
import com.example.project1729.data.keys.RABKIN_RESULTS.SHOW_QUESTION
import com.example.project1729.data.keys.SIVTSEV_RESULTS.SIVTSEV_CURRENT_LEVEL
import com.example.project1729.data.keys.SIVTSEV_RESULTS.SIVTSEV_CURRENT_LEVEL_WRONG
import com.example.project1729.databinding.FragmentRabkinGuideBinding
import com.example.project1729.voice.VoiceAssistant
import com.example.project1729.voice.VoiceAssistantManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class RabkinGuideFragment : Fragment(), VoiceAssistant.VoiceCallback {

    private lateinit var binding: FragmentRabkinGuideBinding
    private lateinit var voiceAssistant: VoiceAssistant
    private val prefs by lazy {
        requireContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    }
    private val HISTORY_HELP_SHOWN_KEY = "help_shown"
    private val COMMANDS_TOAST_SHOWN_KEY_GUIDE = "commands_toast_shown_guide"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentRabkinGuideBinding.inflate(inflater, container, false)
        voiceAssistant = VoiceAssistant(requireContext(), this)
        return binding.root
    }

    private fun showFirstTimeHelp() {
        if (!prefs.getBoolean(HISTORY_HELP_SHOWN_KEY, false)) {
            lifecycleScope.launch {
                delay(500) // Небольшая задержка для полной инициализации UI
                Toast.makeText(
                    context,
                    "Используйте голосовые команды для навигации - скажите: Меню!",
                    Toast.LENGTH_LONG
                ).show()
                prefs.edit().putBoolean(HISTORY_HELP_SHOWN_KEY, true).apply()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        showFirstTimeHelp()
    }

    private fun setupUI() {
        binding.rabkinStartButton.setOnClickListener {
            lifecycleScope.launch {
                delay(200L)
                findNavController().navigate(R.id.action_rabkinGuideFragment_to_rabkinTestFragment)
                SHOW_QUESTION = true
            }
        }

        binding.rabkinTestVoiceButton.setOnLongClickListener {
            showCommandsToast()
            true
        }
        if (CURRENT_MEASURE == "rabkin"){
            binding.rabkinGuideTitle.setText(R.string.rabkin_guide_title)
            binding.rabkinGuideText.setText(R.string.rabkin_guide_text)
            binding.rabkinGuideGiude.setText(R.string.rabkin_guide_guide)
            binding.rabkinGuideImage.setBackgroundResource(R.drawable.rabkin_preview_pic)

            RABKIN_RESULTS.RABKIN_RIGHT_ANSWERS = 0
            RABKIN_RESULTS.RABKIN_WRONG_ANSWERS = 0
            RABKIN_RESULTS.RABKIN_DEITERANOPY_ANSWERS = 0
            RABKIN_RESULTS.RABKIN_PROTANOPY_ANSWERS = 0
        }
        else{
            binding.rabkinGuideTitle.setText(R.string.sivtsev_guide_title)
            binding.rabkinGuideText.setText(R.string.sivtsev_guide_text)
            binding.rabkinGuideGiude.setText(R.string.sivtsev_guide_guide)
            binding.rabkinGuideImage.setBackgroundResource(R.drawable.sivtsev_preview_pic)

            SIVTSEV_CURRENT_LEVEL = 1
            SIVTSEV_CURRENT_LEVEL_WRONG = 0
        }

        binding.rabkinGuideButtonBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.rabkinStartButton.isEnabled = false

        lifecycleScope.launch {

            val startTestingString = getString(R.string.start_testing)
            binding.startButtonLoginText.text = "$startTestingString (6)"
            delay(500L)
            binding.startButtonLoginText.text = "$startTestingString (5)"
            delay(1000L)
            binding.startButtonLoginText.text = "$startTestingString (4)"
            delay(1000L)
            binding.startButtonLoginText.text = "$startTestingString (3)"
            delay(1000L)
            binding.startButtonLoginText.text = "$startTestingString (2)"
            delay(1000L)
            binding.startButtonLoginText.text = "$startTestingString (1)"
            delay(1000L)
            binding.startButtonLoginText.text = startTestingString
            binding.rabkinGuideGiude.setTextColor(requireActivity().getColor(R.color.black))
            binding.rabkinStartButton.isEnabled = true
            binding.rabkinStartButton.background = requireActivity().getDrawable(R.drawable.btn_start)
            binding.startButtonLoginText.setTextColor(requireActivity().getColor(R.color.white))
            binding.rabkinGuideLoader.visibility = View.GONE
        }
        binding.rabkinTestVoiceButton.setOnClickListener {
            if (VoiceAssistantManager.isRecording()) {
                VoiceAssistantManager.stop()
            } else {
                if (checkAudioPermission()) {
                    VoiceAssistantManager.start()
                }
            }
            updateButtonState()
        }
    }

    private fun updateButtonState() {
        binding.rabkinTestVoiceButton.setImageResource(
            if (VoiceAssistantManager.isRecording()) R.drawable.ic_mic_on
            else R.drawable.audio_mic_off_24
        )
    }

    private fun checkAudioPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestAudioPermission() {
        requestPermissions(
            arrayOf(Manifest.permission.RECORD_AUDIO),
            REQUEST_RECORD_AUDIO_PERMISSION
        )
    }
    private fun startTest() {
        lifecycleScope.launch {
            delay(200L)
            findNavController().navigate(R.id.action_rabkinGuideFragment_to_rabkinTestFragment)
            SHOW_QUESTION = true
        }
    }

    private val availableCommands = listOf(
        "начать, вперед," to "Начать тест",
        "меню, команды" to "Показать список доступных команд"
    )

    override fun onVoiceCommandRecognized(command: String) {
        activity?.runOnUiThread {
            when (command) {
                "начать" -> {
                    if (binding.rabkinStartButton.isEnabled) {
                        startTest()
                    } else {
                        Toast.makeText(context, "Дождитесь окончания таймера", Toast.LENGTH_SHORT).show()
                    }
                }
                "вперед" -> {
                    if (binding.rabkinStartButton.isEnabled) {
                        startTest()
                    } else {
                        Toast.makeText(context, "Дождитесь окончания таймера", Toast.LENGTH_SHORT).show()
                    }
                }
                "меню" -> {
                    showCommandsToast()
                }
                "команды" -> {
                    showCommandsToast()

                }
                else -> Toast.makeText(context, "Команда '$command' не поддерживается", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onVoiceTextRecognized(text: String, type: String) {
        activity?.runOnUiThread {
            when (type) {
                "command" -> onVoiceCommandRecognized(text)
                "number" -> handleNumberCommand(text)
                else -> handleCustomLogic(text, type)
            }
        }
    }

    private fun handleNumberCommand(number: String) {
        when (number) {
            "треугольник" -> {
            }
        }
    }

    private fun handleCustomLogic(text: String, type: String?) {
        Toast.makeText(context, "Распознано: $text ($type)", Toast.LENGTH_SHORT).show()
    }

    override fun onError(error: String) {
        activity?.runOnUiThread {
            Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
        }
    }

    private fun showCommandsToast() {
        val commandsText = availableCommands.joinToString("\n") {
            "• ${it.first} - ${it.second}"
        }

        // Создаем кастомный Toast
        val toast = Toast(context).apply {
            duration = Toast.LENGTH_LONG
            view = layoutInflater.inflate(R.layout.toast_wide_layout, null).apply {
                findViewById<TextView>(R.id.toast_text).text = "Доступные команды:\n$commandsText"
            }
        }

        // Настраиваем ширину и позиционирование
        toast.setGravity(Gravity.TOP or Gravity.FILL_HORIZONTAL, 0, 32.toPx())
        toast.show()
    }

    private fun Int.toPx(): Int = (this * resources.displayMetrics.density).toInt()

    override fun onResume() {
        super.onResume()
//        showCommandsToast()
        showCommandsToastIfNeeded()
        VoiceAssistantManager.registerCallback(this)
        updateButtonState()
    }

    private fun showCommandsToastIfNeeded() {
        if (!prefs.getBoolean(COMMANDS_TOAST_SHOWN_KEY_GUIDE, false)) {
            showCommandsToast()
            prefs.edit().putBoolean(COMMANDS_TOAST_SHOWN_KEY_GUIDE, true).apply()
        }
    }

    override fun onPause() {
        super.onPause()
        VoiceAssistantManager.unregisterCallback()
    }

    override fun onMessage(message: String) {
        activity?.runOnUiThread {
            Toast.makeText(context, "Говорите громче!", Toast.LENGTH_SHORT).show()

            lifecycleScope.launch {
                delay(3000)

            }
        }
    }

    companion object {
        private const val REQUEST_RECORD_AUDIO_PERMISSION = 200
    }
}