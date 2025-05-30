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
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.project1729.R
import com.example.project1729.data.keys.RABKIN_RESULTS.CURRENT_MEASURE
import com.example.project1729.data.keys.RABKIN_RESULTS.RABKIN_DEITERANOPY_ANSWERS
import com.example.project1729.data.keys.RABKIN_RESULTS.RABKIN_MAX_RIGHT_ANSWERS
import com.example.project1729.data.keys.RABKIN_RESULTS.RABKIN_MAX_WRONG_ANSWERS
import com.example.project1729.data.keys.RABKIN_RESULTS.RABKIN_PROTANOPY_ANSWERS
import com.example.project1729.data.keys.RABKIN_RESULTS.RABKIN_RIGHT_ANSWERS
import com.example.project1729.data.keys.RABKIN_RESULTS.RABKIN_USED_TESTS
import com.example.project1729.data.keys.RABKIN_RESULTS.RABKIN_WRONG_ANSWERS
import com.example.project1729.data.keys.SIVTSEV_RESULTS.SIVTSEV_RESULT
import com.example.project1729.databinding.FragmentRabkinResultsBinding
import com.example.project1729.ui.view_model.RabkinTestViewModel
import com.example.project1729.voice.VoiceAssistant
import com.example.project1729.voice.VoiceAssistantManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class RabkinResultsFragment : Fragment(), VoiceAssistant.VoiceCallback {

    private lateinit var binding: FragmentRabkinResultsBinding
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private val viewModel by viewModel<RabkinTestViewModel>()
    private lateinit var voiceAssistant: VoiceAssistant

    private val prefs by lazy {
        requireContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    }
    private val HISTORY_HELP_SHOWN_KEY = "help_shown"
    private val COMMANDS_TOAST_SHOWN_KEY_RESULT = "commands_toast_shown_result"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRabkinResultsBinding.inflate(inflater, container, false)

        voiceAssistant = VoiceAssistant(requireContext(), this)
        initializeFragment()
        setupUI()
        showFirstTimeHelp()

        return binding.root
    }

    private fun setupUI() {
        // Voice assistant button click listener
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

        binding.rabkinTestVoiceButton.setOnLongClickListener {
            showCommandsToast()
            true
        }


        binding.rabkinResultsFinishButton.setOnClickListener {

            val navOptions = NavOptions.Builder()
                .setPopUpTo(findNavController().graph.startDestinationId, true) // Очищает весь back stack
                .build()
            findNavController().navigate(R.id.action_rabkinResultsFragment_to_menuFragment, null, navOptions)

            RABKIN_RIGHT_ANSWERS = 0
            RABKIN_WRONG_ANSWERS = 0
            RABKIN_DEITERANOPY_ANSWERS = 0
            RABKIN_PROTANOPY_ANSWERS = 0
            RABKIN_USED_TESTS = mutableListOf()
        }

        binding.rabkinResultsResults.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }

        binding.rabkinResultOverlay.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
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


    companion object {
        private const val REQUEST_RECORD_AUDIO_PERMISSION = 200
    }

    private fun navigateToMenu() {
        val navOptions = NavOptions.Builder()
            .setPopUpTo(findNavController().graph.startDestinationId, true)
            .build()
        findNavController().navigate(R.id.action_rabkinResultsFragment_to_menuFragment, null, navOptions)

        // Reset results
        RABKIN_RIGHT_ANSWERS = 0
        RABKIN_WRONG_ANSWERS = 0
        RABKIN_DEITERANOPY_ANSWERS = 0
        RABKIN_PROTANOPY_ANSWERS = 0
        RABKIN_USED_TESTS = mutableListOf()
    }

    private fun initializeFragment() {
        val resultPicture: Int
        binding.rabkinResultsFinishButton.isEnabled = false
        if (CURRENT_MEASURE == "rabkin") {
            binding.rabkinResultsText.setText(R.string.rabkin_results_text)
            if (RABKIN_RIGHT_ANSWERS >= RABKIN_MAX_RIGHT_ANSWERS) {
                resultPicture = R.drawable.picture_normal_colors
                binding.rabkinResultsResultText.setText(R.string.rabkin_okay)
                binding.rabkinResultBottomSheetTitle.setText(R.string.rabkin_okay)
                binding.rabkinResultBottomSheetText.setText(R.string.rabkin_results_guide_okay)
            } else if (RABKIN_PROTANOPY_ANSWERS == RABKIN_MAX_WRONG_ANSWERS) {
                resultPicture = R.drawable.picture_protanopy
                binding.rabkinResultsResultText.setText(R.string.rabkin_protanopy)
                binding.rabkinResultBottomSheetTitle.setText(R.string.rabkin_protanopy)
                binding.rabkinResultBottomSheetText.setText(R.string.rabkin_results_guide_protanopy)
            } else if (RABKIN_DEITERANOPY_ANSWERS == RABKIN_MAX_WRONG_ANSWERS) {
                resultPicture = R.drawable.picture_deiteranopy
                binding.rabkinResultsResultText.setText(R.string.rabkin_deiteranopy)
                binding.rabkinResultBottomSheetTitle.setText(R.string.rabkin_deiteranopy)
                binding.rabkinResultBottomSheetText.setText(R.string.rabkin_results_guide_deiteranopy)
            } else {
                resultPicture = R.drawable.picture_dihromazy
                binding.rabkinResultsResultText.setText(R.string.rabkin_dihromasuim)
                binding.rabkinResultBottomSheetTitle.setText(R.string.rabkin_dihromasuim)
                binding.rabkinResultBottomSheetText.setText(R.string.rabkin_results_guide_dihromazy)
            }

            Glide.with(binding.rabkinResultsResultImage)
                .load(resultPicture)
                .transform(RoundedCorners(20))
                .placeholder(R.drawable.picture_normal_colors)
                .into(binding.rabkinResultsResultImage)
        } else {
            binding.rabkinResultsText.setText(R.string.sivtsev_results_text)
            if (SIVTSEV_RESULT == "OK") {
                binding.rabkinResultsResultText.setText(R.string.rabkin_okay)
                resultPicture = R.drawable.sivtsev_result_ok
                binding.rabkinResultBottomSheetTitle.setText(R.string.rabkin_okay)
                binding.rabkinResultBottomSheetText.setText(R.string.sivtsev_results_guide_okay)
            } else {
                binding.rabkinResultsResultText.setText(R.string.sivtsev_bad)
                resultPicture = R.drawable.sivtsev_result_bad
                binding.rabkinResultBottomSheetTitle.setText(R.string.sivtsev_bad)
                binding.rabkinResultBottomSheetText.setText(R.string.sivtsev_results_guide_bad)
            }
            Glide.with(binding.rabkinResultsResultImage)
                .load(resultPicture)
                .transform(RoundedCorners(20))
                .placeholder(R.drawable.sivtsev_result_ok)
                .into(binding.rabkinResultsResultImage)
        }

        SIVTSEV_RESULT = "-"
        CURRENT_MEASURE = "-"

        bottomSheetBehavior = BottomSheetBehavior.from(binding.rabkinResultBottomSheet).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN, BottomSheetBehavior.STATE_COLLAPSED -> {
                        binding.rabkinResultOverlay.visibility = View.GONE
                    }
                    else -> {
                        binding.rabkinResultOverlay.visibility = View.VISIBLE
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })

        lifecycleScope.launch {
            binding.rabkinResultsFinishButton.background =
                requireActivity().getDrawable(R.drawable.btn_start)
            binding.rabkinResultsFinishButtonText.setTextColor(requireActivity().getColor(R.color.white))
            delay(1000L)
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            binding.rabkinResultsFinishButton.isEnabled = true
            binding.rabkinResultsFinishButton.visibility = View.VISIBLE
            binding.rabkinResultsFinishButtonText.visibility = View.VISIBLE

            delay(10000L)
            if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
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
        VoiceAssistantManager.registerCallback(this)
        updateButtonState()
    }

    private fun showCommandsToastIfNeeded() {
        if (!prefs.getBoolean(COMMANDS_TOAST_SHOWN_KEY_RESULT, false)) {
            showCommandsToast()
            prefs.edit().putBoolean(COMMANDS_TOAST_SHOWN_KEY_RESULT, true).apply()
        }
    }

    override fun onPause() {
        super.onPause()
        VoiceAssistantManager.unregisterCallback()
    }

    private val availableCommands = listOf(
        "вперед, завершить" to "Завершить тест и вернуться в меню",
        "меню, команды" to "Показать список доступных команд"
    )

    override fun onVoiceCommandRecognized(command: String) {
        activity?.runOnUiThread {
            when (command) {
                "завершить" -> {
                    activity?.runOnUiThread {
                        navigateToMenu()
                    }
                }
                "вперед" -> {
                    activity?.runOnUiThread {
                        navigateToMenu()
                    }
                }
                "меню" -> {
                    showCommandsToast()
                }
                "команды" -> {
                    showCommandsToast()
                }
                else -> Toast.makeText(
                    context,
                    "Команда '$command' не поддерживается",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    }

    private fun updateButtonState() {
        binding.rabkinTestVoiceButton.setImageResource(
            if (VoiceAssistantManager.isRecording()) R.drawable.ic_mic_on
            else R.drawable.audio_mic_off_24
        )
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

    override fun onMessage(message: String) {
        activity?.runOnUiThread {
            Toast.makeText(context, "Говорите громче!", Toast.LENGTH_SHORT).show()

            lifecycleScope.launch {
                delay(3000)

            }
        }
    }

}