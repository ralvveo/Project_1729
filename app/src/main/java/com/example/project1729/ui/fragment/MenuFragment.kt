package com.example.project1729.ui.fragment

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.project1729.R
import com.example.project1729.data.keys.RABKIN_RESULTS.CURRENT_MEASURE
import com.example.project1729.databinding.FragmentMenuBinding
import com.example.project1729.voice.VoiceAssistant
import com.example.project1729.voice.VoiceAssistantManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MenuFragment : Fragment(), VoiceAssistant.VoiceCallback {

    private lateinit var binding: FragmentMenuBinding
    private lateinit var voiceAssistant: VoiceAssistant
    private val prefs by lazy {
        requireContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    }
    private val HISTORY_HELP_SHOWN_KEY = "help_shown"
    private val COMMANDS_TOAST_SHOWN_KEY_MENU = "commands_toast_shown_key_menu"


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMenuBinding.inflate(inflater, container, false)
        voiceAssistant = VoiceAssistant(requireContext(), this)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        VoiceAssistantManager.initialize(requireContext(), this)
        updateButtonState()
        setupUI()
        showFirstTimeHelp()
        requestAudioPermission()

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

    private fun setupUI() {

        val sharedPrefs = requireActivity().getSharedPreferences(
            PROJECT1729_PREFERENCES,
            Application.MODE_PRIVATE
        )
        val userName = sharedPrefs.getString(USERNAME, "Local_user")
        binding.menuProfileName.text = userName
        binding.menuProfileImage.setOnClickListener {
            showExitDialog()
        }
        binding.menuProfileName.setOnClickListener {
            showExitDialog()
        }
        binding.menuProfileArrow.setOnClickListener {
            showExitDialog()
        }
        binding.menuFirstItem.setOnClickListener {
            findNavController().navigate(R.id.action_menuFragment_to_rabkinGuideFragment)
            CURRENT_MEASURE = "rabkin"
        }

        binding.menuSecondItem.setOnClickListener {
            findNavController().navigate(R.id.action_menuFragment_to_rabkinGuideFragment)
            CURRENT_MEASURE = "sivtsev"
        }

        binding.menuThirdItem.setOnClickListener {
            findNavController().navigate(R.id.action_menuFragment_to_kchsmFragment)
        }

        binding.menuProfileHistoryButton.setOnClickListener {
            findNavController().navigate(R.id.action_menuFragment_to_historyMainFragment)
        }

        binding.rabkinTestVoiceButton.setOnLongClickListener {
            showCommandsToast()
            true
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

    private fun showExitDialog(){
        MaterialAlertDialogBuilder(requireActivity(), R.style.MaterialAlertDialog2)
            .setTitle(getString(R.string.exit_account_title)) // Заголовок диалога
            .setMessage(getString(R.string.exit_account_descr)) // Описание диалога
            .setNeutralButton(R.string.exit_account_cancel) { dialog, which -> // Добавляет кнопку «Отмена»
                // Действия, выполняемые при нажатии на кнопку «Отмена»
            }

            .setPositiveButton(R.string.exit_account_confirm) { dialog, which -> // Добавляет кнопку «Да»
                val sharedPrefs = requireActivity().getSharedPreferences(PROJECT1729_PREFERENCES, Application.MODE_PRIVATE)
                sharedPrefs.edit()
                    .putString(USERNAME, NOT_AUTHORIZED)
                    .apply()

                val navOptions = NavOptions.Builder()
                    .setPopUpTo(findNavController().graph.startDestinationId, true) // Очищает весь back stack
                    .build()
                findNavController().navigate(R.id.action_menuFragment_to_startFragment, null, navOptions)
            }
            .show()
    }

    companion object{
        const val PROJECT1729_PREFERENCES = "PROJECT1729_PREFERENCES"
        const val USERNAME = "USERNAME"
        const val NOT_AUTHORIZED = "NOT_AUTHORIZED"
        private const val REQUEST_RECORD_AUDIO_PERMISSION = 200
    }

    private val availableCommands = listOf(
        "Цветовосприятие" to "Выбрать цветовосприятие",
        "Острота зрения" to "Выбрать остроту зрения",
        "Световые мелькания" to "Выбрать световые мелькания",
        "Команды" to "Показать список доступных команд",
        "Вперед, история" to "Перейти к меню Истории",
    )

    override fun onVoiceCommandRecognized(command: String) {
        activity?.runOnUiThread {
            when (command) {
                "меню" -> {
                    showCommandsToast()
                }
                "команды" -> {
                    showCommandsToast()
                }
                "история" -> {
                    findNavController().navigate(R.id.action_menuFragment_to_historyMainFragment)
                }

                "вперед" -> {
                    findNavController().navigate(R.id.action_menuFragment_to_historyMainFragment)
                }
                "цветоощущение" -> {
                    findNavController().navigate(R.id.action_menuFragment_to_rabkinGuideFragment)
                    CURRENT_MEASURE = "rabkin"
                }

                "световые мелькания" -> {
                    findNavController().navigate(R.id.action_menuFragment_to_kchsmFragment)
                }

                "первый тест" -> {
                    findNavController().navigate(R.id.action_menuFragment_to_rabkinGuideFragment)
                    CURRENT_MEASURE = "rabkin"
                }

                "один" -> {
                    findNavController().navigate(R.id.action_menuFragment_to_rabkinGuideFragment)
                    CURRENT_MEASURE = "rabkin"
                }

                "цветовосприятие" -> {
                    findNavController().navigate(R.id.action_menuFragment_to_rabkinGuideFragment)
                    CURRENT_MEASURE = "rabkin"
                }

                "острота зрения" -> {
                    findNavController().navigate(R.id.action_menuFragment_to_rabkinGuideFragment)
                    CURRENT_MEASURE = "sivtsev"
                }

                "второй тест" -> {
                    findNavController().navigate(R.id.action_menuFragment_to_rabkinGuideFragment)
                    CURRENT_MEASURE = "sivtsev"
                }

                "первый" -> {
                    findNavController().navigate(R.id.action_menuFragment_to_rabkinGuideFragment)
                    CURRENT_MEASURE = "rabkin"
                }
                "второй" -> {
                    findNavController().navigate(R.id.action_menuFragment_to_rabkinGuideFragment)
                    CURRENT_MEASURE = "sivtsev"
                }

                "два" -> {
                    findNavController().navigate(R.id.action_menuFragment_to_rabkinGuideFragment)
                    CURRENT_MEASURE = "sivtsev"
                }

                else -> Toast.makeText(
                    context,
                    "Команда '$command' не поддерживается",
                    Toast.LENGTH_SHORT
                ).show()
            }
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

    }

    private fun showCommandsToastIfNeeded() {
        if (!prefs.getBoolean(COMMANDS_TOAST_SHOWN_KEY_MENU, false)) {
            showCommandsToast()
            prefs.edit().putBoolean(COMMANDS_TOAST_SHOWN_KEY_MENU, true).apply()
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

    private fun handleCustomLogic(text: String, type: String?) {
        Toast.makeText(context, "Распознано: $text ($type)", Toast.LENGTH_SHORT).show()
    }

    private fun handleNumberCommand(number: String) {
        when (number) {
            "один" -> {
                findNavController().navigate(R.id.action_menuFragment_to_rabkinGuideFragment)
                CURRENT_MEASURE = "rabkin"
            }
            "два" -> {
                findNavController().navigate(R.id.action_menuFragment_to_rabkinGuideFragment)
                CURRENT_MEASURE = "sivtsev"
            }
            "первый" -> {
                findNavController().navigate(R.id.action_menuFragment_to_rabkinGuideFragment)
                CURRENT_MEASURE = "rabkin"
            }
            "второй" -> {
                findNavController().navigate(R.id.action_menuFragment_to_rabkinGuideFragment)
                CURRENT_MEASURE = "sivtsev"
            }
            "цветовосприятие" -> {
                findNavController().navigate(R.id.action_menuFragment_to_rabkinGuideFragment)
                CURRENT_MEASURE = "rabkin"
            }
            "острота зрения" -> {
                findNavController().navigate(R.id.action_menuFragment_to_rabkinGuideFragment)
                CURRENT_MEASURE = "sivtsev"
            }
            "треугольник" -> {

            }
        }
    }

    override fun onError(error: String) {
        activity?.runOnUiThread {
            Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
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


}