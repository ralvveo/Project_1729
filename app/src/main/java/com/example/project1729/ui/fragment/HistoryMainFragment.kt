package com.example.project1729.ui.fragment

import android.Manifest
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
import androidx.navigation.fragment.findNavController
import com.example.project1729.R
import com.example.project1729.data.keys.RABKIN_RESULTS.CURRENT_MEASURE
import com.example.project1729.databinding.FragmentHistoryMainBinding
import com.example.project1729.voice.VoiceAssistant
import com.example.project1729.voice.VoiceAssistantManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class HistoryMainFragment : Fragment(), VoiceAssistant.VoiceCallback {

    private lateinit var binding: FragmentHistoryMainBinding
    private lateinit var voiceAssistant: VoiceAssistant
    private val prefs by lazy {
        requireContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    }
    private val HISTORY_HELP_SHOWN_KEY = "help_shown"
    private val COMMANDS_TOAST_SHOWN_MAIN_HISTORY = "commands_toast_shown_main_history"


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentHistoryMainBinding.inflate(inflater, container, false)
        voiceAssistant = VoiceAssistant(requireContext(), this)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
        showFirstTimeHelp()

//        binding.historyMainThirdItem.setOnClickListener {
//            findNavController().navigate(
//                R.id.action_historyMainFragment_to_historyContentFragment,
//                HistoryContentFragment.createArgs(HISTORY_CONTENT_KCHSM)
//            )
//        }
//
//        binding.historyMainFourthItem.setOnClickListener {
//            findNavController().navigate(
//                R.id.action_historyMainFragment_to_historyContentFragment,
//                HistoryContentFragment.createArgs(HISTORY_CONTENT_KCHSM)
//            )
//        }
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

        binding.rabkinTestVoiceButton.setOnLongClickListener {
            showCommandsToast()
            true
        }

        binding.historyMainButtonBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.historyMainFirstItem.setOnClickListener {
            findNavController().navigate(
                R.id.action_historyMainFragment_to_historyContentFragment,
                HistoryContentFragment.createArgs(HISTORY_CONTENT_RABKIN)
            )
        }

        binding.historyMainSecondItem.setOnClickListener {
            findNavController().navigate(
                R.id.action_historyMainFragment_to_historyContentFragment,
                HistoryContentFragment.createArgs(HISTORY_CONTENT_SIVTSEV)
            )
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

    companion object{
        const val HISTORY_CONTENT_RABKIN = "history_content_rabkin"
        const val HISTORY_CONTENT_SIVTSEV = "history_content_sivtsev"
        const val HISTORY_CONTENT_KCHSM = "history_content_KCHSM"
        const val HISTORY_CONTENT_PHOTO = "history_content_photo"
        private const val REQUEST_RECORD_AUDIO_PERMISSION = 200
    }

    private val availableCommands = listOf(
        "цветовосприятие, первый" to "Выбрать цветовосприятие",
        "острота зрения, второй" to "Выбрать остроту зрения",
        "меню, команды" to "Показать список доступных команд",
        "вперед, завершить" to "Завершить просмотр истории",
    )

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
            "один" -> {
                findNavController().navigate(
                    R.id.action_historyMainFragment_to_historyContentFragment,
                    HistoryContentFragment.createArgs(HISTORY_CONTENT_RABKIN)
                )
            }
            "два" -> {
                findNavController().navigate(
                    R.id.action_historyMainFragment_to_historyContentFragment,
                    HistoryContentFragment.createArgs(HISTORY_CONTENT_SIVTSEV)
                )
            }
            "первый" -> {
                findNavController().navigate(
                    R.id.action_historyMainFragment_to_historyContentFragment,
                    HistoryContentFragment.createArgs(HISTORY_CONTENT_RABKIN)
                )
            }
            "второй" -> {
                findNavController().navigate(
                    R.id.action_historyMainFragment_to_historyContentFragment,
                    HistoryContentFragment.createArgs(HISTORY_CONTENT_SIVTSEV)
                )
            }
            "цветовосприятие" -> {
                findNavController().navigate(
                    R.id.action_historyMainFragment_to_historyContentFragment,
                    HistoryContentFragment.createArgs(HISTORY_CONTENT_RABKIN)
                )
            }
            "острота зрения" -> {
                findNavController().navigate(
                    R.id.action_historyMainFragment_to_historyContentFragment,
                    HistoryContentFragment.createArgs(HISTORY_CONTENT_SIVTSEV)
                )
            }
            "треугольник" -> {

            }
        }
    }

    private fun handleCustomLogic(text: String, type: String?) {
        Toast.makeText(context, "Распознано: $text ($type)", Toast.LENGTH_SHORT).show()
    }

    override fun onVoiceCommandRecognized(command: String) {
        activity?.runOnUiThread {
            when (command) {
                "меню" -> {
                    showCommandsToast()

                }
                "команды" -> {
                    showCommandsToast()
                }
                "назад" -> {
                    findNavController().navigateUp()
                }

                "завершить" -> {
                    findNavController().navigateUp()
                }

                "вперед" -> {
                    findNavController().navigateUp()
                }
                "цветоощущение" -> {
                    findNavController().navigate(
                        R.id.action_historyMainFragment_to_historyContentFragment,
                        HistoryContentFragment.createArgs(HISTORY_CONTENT_RABKIN)
                    )
                }

                "первый тест" -> {
                    findNavController().navigate(
                        R.id.action_historyMainFragment_to_historyContentFragment,
                        HistoryContentFragment.createArgs(HISTORY_CONTENT_RABKIN)
                    )
                }

                "один" -> {
                    findNavController().navigate(
                        R.id.action_historyMainFragment_to_historyContentFragment,
                        HistoryContentFragment.createArgs(HISTORY_CONTENT_RABKIN)
                    )
                }

                "цветовосприятие" -> {
                    findNavController().navigate(
                        R.id.action_historyMainFragment_to_historyContentFragment,
                        HistoryContentFragment.createArgs(HISTORY_CONTENT_RABKIN)
                    )
                }

                "первый" -> {
                    findNavController().navigate(
                        R.id.action_historyMainFragment_to_historyContentFragment,
                        HistoryContentFragment.createArgs(HISTORY_CONTENT_RABKIN)
                    )
                }
                "второй" -> {
                    findNavController().navigate(
                        R.id.action_historyMainFragment_to_historyContentFragment,
                        HistoryContentFragment.createArgs(HISTORY_CONTENT_SIVTSEV)
                    )
                }

                "острота зрения" -> {
                    findNavController().navigate(
                        R.id.action_historyMainFragment_to_historyContentFragment,
                        HistoryContentFragment.createArgs(HISTORY_CONTENT_SIVTSEV)
                    )
                }

                "два" -> {
                    findNavController().navigate(
                        R.id.action_historyMainFragment_to_historyContentFragment,
                        HistoryContentFragment.createArgs(HISTORY_CONTENT_SIVTSEV)
                    )
                }

                "второй тест" -> {
                    findNavController().navigate(
                        R.id.action_historyMainFragment_to_historyContentFragment,
                        HistoryContentFragment.createArgs(HISTORY_CONTENT_SIVTSEV)
                    )
                }

                else -> Toast.makeText(
                    context,
                    "Команда '$command' не поддерживается",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
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
        showCommandsToastIfNeeded()

        VoiceAssistantManager.registerCallback(this)
        updateButtonState()
    }

    private fun showCommandsToastIfNeeded() {
        if (!prefs.getBoolean(COMMANDS_TOAST_SHOWN_MAIN_HISTORY, false)) {
            showCommandsToast()
            prefs.edit().putBoolean(COMMANDS_TOAST_SHOWN_MAIN_HISTORY, true).apply()
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