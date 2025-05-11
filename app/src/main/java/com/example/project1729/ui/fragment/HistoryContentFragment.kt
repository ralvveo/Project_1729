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
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project1729.R
import com.example.project1729.databinding.FragmentHistoryContentBinding
import com.example.project1729.domain.model.Test
import com.example.project1729.ui.adapters.HistoryContentAdapter
import com.example.project1729.ui.view_model.HistoryContentViewModel
import com.example.project1729.voice.VoiceAssistant
import com.example.project1729.voice.VoiceAssistantManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class HistoryContentFragment : Fragment(), VoiceAssistant.VoiceCallback {


    private lateinit var binding: FragmentHistoryContentBinding
    private lateinit var voiceAssistant: VoiceAssistant

    private var currentMeasure = HISTORY_CONTENT_RABKIN
    private val viewModel by viewModel<HistoryContentViewModel>()

    private val prefs by lazy {
        requireContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    }
    private val HISTORY_HELP_SHOWN_KEY = "help_shown"
    private val COMMANDS_TOAST_SHOWN_HISTORY = "commands_toast_shown_history"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHistoryContentBinding.inflate(inflater, container, false)
        voiceAssistant = VoiceAssistant(requireContext(), this)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        showFirstTimeHelp()

    }

    private fun updateButtonState() {
        binding.rabkinTestVoiceButton.setImageResource(
            if (VoiceAssistantManager.isRecording()) R.drawable.ic_mic_on
            else R.drawable.audio_mic_off_24
        )
    }

    private fun setupUI() {
        currentMeasure = requireArguments().getString(CURRENT_MEASURE)?: HISTORY_CONTENT_RABKIN

        binding.historyContentButtonBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.rabkinTestVoiceButton.setOnLongClickListener {
            showCommandsToast()
            true
        }

        binding.historyContentClearButton.setOnClickListener {
            deleteHistory()
        }

        viewModel.getHistoryContentLiveData().observe(viewLifecycleOwner){ historyList ->

            render(historyList)

        }
        initializeFragment()

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

    private fun render(historyList: MutableList<Test>){
        var showList: MutableList<Test>
        binding.historyContentList.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, true)
        when (currentMeasure){
            HISTORY_CONTENT_RABKIN -> {
                showList = historyList.filter { test -> test.type == "Rabkin" }.toMutableList()
            }
            else -> {
                showList = historyList.filter { test -> test.type == "Sivtsev" }.toMutableList()
            }
        }
        if (showList.isEmpty()){
            binding.historyContentPlaceholderText.visibility = View.VISIBLE
            binding.historyContentPlaceholderTitle.visibility = View.VISIBLE
            binding.historyContentPlaceholderImage.visibility = View.VISIBLE
            binding.historyContentList.visibility = View.GONE
            binding.historyContentClearButton.visibility = View.GONE
            binding.historyContentClearButtonText.visibility = View.GONE
            binding.historyContentText.visibility = View.GONE
        }

        else{
            binding.historyContentPlaceholderText.visibility = View.GONE
            binding.historyContentPlaceholderTitle.visibility = View.GONE
            binding.historyContentPlaceholderImage.visibility = View.GONE
            binding.historyContentList.visibility = View.VISIBLE
            binding.historyContentClearButton.visibility = View.VISIBLE
            binding.historyContentClearButtonText.visibility = View.VISIBLE
            binding.historyContentText.visibility = View.VISIBLE
        }
        binding.historyContentList.adapter = HistoryContentAdapter(showList)
    }

    private fun initializeFragment(){

        when (currentMeasure){
            HISTORY_CONTENT_RABKIN -> {
                binding.historyContentTitle.setText(R.string.history_rabkin_title)
                binding.historyContentText.setText(R.string.history_rabkin_text)
                binding.historyContentImage.background = requireActivity().getDrawable(R.drawable.picture_history_rabkin)
            }

            else -> {
                binding.historyContentTitle.setText(R.string.history_sivtsev_title)
                binding.historyContentText.setText(R.string.history_sivtsev_text)
                binding.historyContentImage.background = requireActivity().getDrawable(R.drawable.picture_history_sivtsev)
            }
        }


    }

    private fun deleteHistory(){
        MaterialAlertDialogBuilder(requireActivity(), R.style.MaterialAlertDialog2)
            .setTitle(getString(R.string.delete_title)) // Заголовок диалога
            .setMessage(getString(R.string.delete_descr)) // Описание диалога
            .setNeutralButton(R.string.delete_cancel) { dialog, which -> // Добавляет кнопку «Отмена»
                // Действия, выполняемые при нажатии на кнопку «Отмена»
            }

            .setPositiveButton(R.string.delete_confirm) { dialog, which -> // Добавляет кнопку «Да»
                var deleteTest = "Rabkin"
                if (currentMeasure == HISTORY_CONTENT_SIVTSEV){
                    deleteTest = "Sivtsev"
                }
                viewModel.deleteTests(deleteTest)
                lifecycleScope.launch {
                    delay(300L)
                    viewModel.checkData()
                }

                val toast = Toast.makeText(requireActivity(), "История успешно очищена", Toast.LENGTH_SHORT)
                toast.show()
            }
            .show()
    }

    companion object{
        private const val CURRENT_MEASURE = "current_measure"
        fun createArgs(currentMeasure: String): Bundle = bundleOf(CURRENT_MEASURE to currentMeasure)
        const val HISTORY_CONTENT_RABKIN = "history_content_rabkin"
        const val HISTORY_CONTENT_SIVTSEV = "history_content_sivtsev"
        const val HISTORY_CONTENT_KCHSM = "history_content_KCHSM"
        const val HISTORY_CONTENT_PHOTO = "history_content_photo"
        private const val REQUEST_RECORD_AUDIO_PERMISSION = 200
    }

    private val availableCommands = listOf(
        "назад, вперед, завершить" to "Вернуться назад",
        "меню, команды" to "Показать список доступных команд"
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
                "назад" -> {
                    findNavController().navigateUp()
                }

                "завершить" -> {
                    findNavController().navigateUp()
                }

                "вперед" -> {
                    findNavController().navigateUp()
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
        showCommandsToastIfNeeded()

//        showCommandsToast()

        VoiceAssistantManager.registerCallback(this)
        updateButtonState()
    }

    private fun showCommandsToastIfNeeded() {
        if (!prefs.getBoolean(COMMANDS_TOAST_SHOWN_HISTORY, false)) {
            showCommandsToast()
            prefs.edit().putBoolean(COMMANDS_TOAST_SHOWN_HISTORY, true).apply()
        }
    }

    override fun onPause() {
        super.onPause()
        VoiceAssistantManager.unregisterCallback()
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
            "треугольник" -> {

            }
        }
    }

    override fun onError(error: String) {
        activity?.runOnUiThread {
            Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
        }
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