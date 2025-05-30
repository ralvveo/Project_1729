package com.example.project1729.ui.fragment

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
import com.example.project1729.data.keys.RABKIN_RESULTS
import com.example.project1729.data.keys.RABKIN_RESULTS.CURRENT_MEASURE
import com.example.project1729.data.keys.RABKIN_RESULTS.SHOW_QUESTION
import com.example.project1729.data.keys.SIVTSEV_RESULTS.SIVTSEV_CURRENT_LEVEL
import com.example.project1729.data.keys.SIVTSEV_RESULTS.SIVTSEV_CURRENT_LEVEL_WRONG
import com.example.project1729.databinding.FragmentKchsmBinding
import com.example.project1729.domain.model.KchsmMeasure
import com.example.project1729.ui.view_model.KchsmViewModel
import com.example.project1729.voice.VoiceAssistant
import com.example.project1729.voice.VoiceAssistantManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class KchsmFragment : Fragment(), VoiceAssistant.VoiceCallback {

    private lateinit var binding: FragmentKchsmBinding
    private var eyeState = STATE_RIGHT
    private var connectState = STATE_UNCONNECTED
    private var buttonState = STATE_RED
    private val viewModel by viewModel<KchsmViewModel>()
    private lateinit var voiceAssistant: VoiceAssistant
    private val prefs by lazy {
        requireContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    }
    private val COMMANDS_TOAST_SHOWN_KEY_KCHSM_START = "commands_toast_shown_kchsm_start"


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentKchsmBinding.inflate(inflater, container, false)
        voiceAssistant = VoiceAssistant(requireContext(), this)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
    }

    private fun setupUI() {
        binding.testingEyeButton1.setOnClickListener {
            eyeState = STATE_LEFT
            renderButton()
        }
        binding.testingEyeButton2.setOnClickListener {
            eyeState = STATE_RIGHT
            renderButton()
        }

        binding.redButton.setOnClickListener {
            buttonState = STATE_RED
            renderButton()
        }

        binding.greenButton.setOnClickListener {
            buttonState = STATE_GREEN
            renderButton()
        }

        binding.blueButton.setOnClickListener {
            buttonState = STATE_BLUE
            renderButton()
        }

        binding.kchsmConnectionButton.setOnClickListener {
            if (connectState == STATE_UNCONNECTED){
                findNavController().navigate(R.id.action_kchsmFragment_to_deviceFragment)
            }
            else{
                viewModel.unconnect()
            }
        }

        binding.kchsmButtonStart.setOnClickListener {
            handleStartOrConnect()
        }

        viewModel.getDeviceLiveData().observe(viewLifecycleOwner) {
                message -> render(message)
        }
        viewModel.getResultLiveData().observe(viewLifecycleOwner){
                kchsmMeasure -> render(kchsmMeasure)
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

    // Обработка кнопки "Начать/Подключиться" и команды "вперед"
    private fun handleStartOrConnect() {
        if (connectState == STATE_UNCONNECTED) {
            viewModel.connect()
        } else {
            startMeasurementAndNavigate()
        }
    }

    // Обработка команды "завершить"
    private fun handleFinishOrDisconnect() {
        if (connectState == STATE_CONNECTED) {
            viewModel.unconnect()
            Toast.makeText(context, "Прибор отключен", Toast.LENGTH_SHORT).show()
        } else {
            findNavController().navigateUp()
        }
    }

    // Запуск измерения и переход к тесту
    private fun startMeasurementAndNavigate() {
        val currentEye = if (eyeState == STATE_LEFT) "L" else "R"
        val currentColor = when (buttonState) {
            STATE_RED -> "R"
            STATE_GREEN -> "G"
            STATE_BLUE -> "B"
            else -> "R" // По умолчанию красный
        }
        viewModel.sendMessage("D$currentColor$currentEye")
        findNavController().navigate(R.id.action_kchsmFragment_to_buttonFragment)
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

    private fun render(message: String){
        when (message){
            " " -> {}
            "connected"-> {
                binding.testingEyeSelector.visibility = View.VISIBLE
                binding.kchsmText2.visibility = View.VISIBLE
                binding.kchsmResult.visibility = View.VISIBLE
                binding.kchsmPlaceholder.visibility = View.GONE
                binding.redButton.visibility = View.VISIBLE
                binding.blueButton.visibility = View.VISIBLE
                binding.greenButton.visibility = View.VISIBLE
                binding.diodeColorText.visibility = View.VISIBLE
                binding.kchsmButtonStartText.text = "Начать измерение"
                binding.kchsmConnectionButtonFirstImage.background = requireActivity().getDrawable(R.drawable.green_tick)
                binding.kchsmConnectionButtonSecondImage.background = requireActivity().getDrawable(R.drawable.green_tick)
                binding.kchsmConnectionButton.background = requireActivity().getDrawable(R.drawable.shape_green)
                binding.kchsmConnectionButtonText.text = "Подключено"
                binding.kchsmResultText.text = "Можно начинать измерение КЧСМ"
                binding.kchsmResultText.setTextColor((getResources().getColor(R.color.blue)))
                connectState = STATE_CONNECTED
            }
            "not_connected"-> {
                binding.testingEyeSelector.visibility = View.GONE
                binding.kchsmText2.visibility = View.GONE
                binding.kchsmResult.visibility = View.GONE
                binding.kchsmPlaceholder.visibility = View.VISIBLE
                binding.redButton.visibility = View.GONE
                binding.blueButton.visibility = View.GONE
                binding.greenButton.visibility = View.GONE
                binding.diodeColorText.visibility = View.GONE
                binding.kchsmButtonStartText.text = "Подключиться"
                binding.kchsmConnectionButtonFirstImage.background = requireActivity().getDrawable(R.drawable.red_cross)
                binding.kchsmConnectionButtonSecondImage.background = requireActivity().getDrawable(R.drawable.red_cross)
                binding.kchsmConnectionButton.background = requireActivity().getDrawable(R.drawable.shape_red)
                binding.kchsmConnectionButtonText.text = "Не Подключено"
                binding.kchsmResultText.text = "Выберите прибор КЧСМ при помощи\nкрасной кнопки и подключитесь к нему"
                binding.kchsmResultText.setTextColor((getResources().getColor(R.color.red)))
                connectState = STATE_UNCONNECTED
            }
            else -> {}
        }
    }

    private fun render (kchsmMeasure: KchsmMeasure){
        binding.kchsmResult.text = "${kchsmMeasure.critical_frequency} Гц"
        if (kchsmMeasure.critical_frequency.toInt() <= 30){
            binding.kchsmResultText.text = "КЧСМ ниже нормы. Реомендуется попробовать снова или обратиться к специалисту."
        }
        else if ((kchsmMeasure.critical_frequency.toInt() > 30) and (kchsmMeasure.critical_frequency.toInt() <= 60)){
            binding.kchsmResultText.text = "Значение КЧСМ соответствует норме!"
        }
        else{
            binding.kchsmResultText.text = "Слишком высокое значение КЧСМ. Попробуйте провести измерение еще раз."
        }

    }

    private fun renderButton(){
        when (eyeState){
            STATE_RIGHT -> {
                binding.testingEyeButton1.setBackgroundColor(getResources().getColor(R.color.white))
                binding.testingEyeButton2.setBackgroundColor(getResources().getColor(R.color.blue))

                binding.testingEyeButton1.setTextColor((getResources().getColor(R.color.black)))
                binding.testingEyeButton2.setTextColor((getResources().getColor(R.color.white)))
            }
            else -> {
                binding.testingEyeButton1.setBackgroundColor(getResources().getColor(R.color.blue))
                binding.testingEyeButton2.setBackgroundColor(getResources().getColor(R.color.white))

                binding.testingEyeButton1.setTextColor((getResources().getColor(R.color.white)))
                binding.testingEyeButton2.setTextColor((getResources().getColor(R.color.black)))
            }
        }

        when (buttonState){
            STATE_RED -> {
                binding.redButton.background = requireActivity().getDrawable(R.drawable.red_btn_active)
                binding.greenButton.background = requireActivity().getDrawable(R.drawable.green_btn_inactive)
                binding.blueButton.background = requireActivity().getDrawable(R.drawable.blue_btn_inactive)
            }
            STATE_GREEN -> {
                binding.redButton.background = requireActivity().getDrawable(R.drawable.red_btn_inactive)
                binding.greenButton.background = requireActivity().getDrawable(R.drawable.green_btn_active)
                binding.blueButton.background = requireActivity().getDrawable(R.drawable.blue_btn_inactive)
            }
            else -> {
                binding.redButton.background = requireActivity().getDrawable(R.drawable.red_btn_inactive)
                binding.greenButton.background = requireActivity().getDrawable(R.drawable.green_btn_inactive)
                binding.blueButton.background = requireActivity().getDrawable(R.drawable.blue_btn_active)
            }
        }
    }

    companion object{
        private const val STATE_LEFT = "state_left"
        private const val STATE_RIGHT = "state_right"
        private const val STATE_CONNECTED = "state_connected"
        private const val STATE_UNCONNECTED = "state_unconnected"
        private const val STATE_RED = "state_red"
        private const val STATE_BLUE = "state_blue"
        private const val STATE_GREEN = "state_green"
        private const val REQUEST_RECORD_AUDIO_PERMISSION = 200
    }

    private val availableCommands = listOf(
        "вперед" to "Подключиться или начать тест",
        "завершить" to "Отключиться или вернуться",
        "левый" to "Выбрать левый глаз",
        "правый" to "Выбрать правый глаз",
        "команды" to "Показать список команд"
    )

    override fun onVoiceCommandRecognized(command: String) {
        activity?.runOnUiThread {
            when (command) {
                "вперед" -> {
                    handleStartOrConnect()
                    Toast.makeText(context, "Выполняю: $command", Toast.LENGTH_SHORT).show()
                }
                "завершить" -> {
                    handleFinishOrDisconnect()
                    Toast.makeText(context, "Выполняю: $command", Toast.LENGTH_SHORT).show()
                }
                "левый" -> {
                    eyeState = STATE_LEFT
                    renderButton()
                    Toast.makeText(context, "Выбран левый глаз", Toast.LENGTH_SHORT).show()
                }
                "правый" -> {
                    eyeState = STATE_RIGHT
                    renderButton()
                    Toast.makeText(context, "Выбран правый глаз", Toast.LENGTH_SHORT).show()
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
                "number" -> handleNumberCommand(text)
                else -> handleCustomLogic(text, type)
            }
        }
    }

    private fun handleNumberCommand(number: String) {
        when (number) {
            "треугольник" -> {
                // Логика для треугольников
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
        if (!prefs.getBoolean(COMMANDS_TOAST_SHOWN_KEY_KCHSM_START, false)) {
            showCommandsToast()
            prefs.edit().putBoolean(COMMANDS_TOAST_SHOWN_KEY_KCHSM_START, true).apply()
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