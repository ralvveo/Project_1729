package com.example.project1729.ui.fragment

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Rect
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.project1729.R
import com.example.project1729.data.keys.RABKIN_RESULTS.CURRENT_MEASURE
import com.example.project1729.data.keys.RABKIN_RESULTS.SHOW_QUESTION
import com.example.project1729.databinding.FragmentRabkinTestBinding
import com.example.project1729.domain.model.RabkinTest
import com.example.project1729.domain.model.SivtsevTest
import com.example.project1729.ui.view_model.RabkinTestViewModel
import com.example.project1729.voice.VoiceAssistant
import com.example.project1729.voice.VoiceAssistantManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class RabkinTestFragment : Fragment(), VoiceAssistant.VoiceCallback {

    private lateinit var binding: FragmentRabkinTestBinding
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private val viewModel by viewModel<RabkinTestViewModel>()
    private var justStarted = true
    private var currentText = ""
    private var currentRabkinTest = RabkinTest("", "", "", "")
    private var currentSivtsevTest = SivtsevTest("", "", "")
    private lateinit var voiceAssistant: VoiceAssistant
    private val prefs by lazy {
        requireContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    }
    private val COMMANDS_TOAST_SHOWN_KEY = "commands_toast_shown_key"
    private val backPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            showExitDialog()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRabkinTestBinding.inflate(inflater, container, false)
        voiceAssistant = VoiceAssistant(requireContext(), this)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, backPressedCallback)

        setupUI()
        initializeFragment()
        setupObservers()
        setupBottomSheet()
    }

    private fun showVoiceCommandsDialog() {
        MaterialAlertDialogBuilder(requireActivity(), R.style.MaterialAlertDialog2)
            .setTitle("Голосовые команды")
            .setMessage("В этом разделе доступны голосовые команды:\n\n• \"продолжить\" - для продолжения теста\n• \"назад\" - для возврата в меню")
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .setCancelable(false)
            .show()
    }

    private fun setupUI() {
        binding.apply {
            rabkinTestButtonBack.setOnClickListener { showExitDialog() }
            rabkinTestInput.requestFocus()

            rabkinTestInput.doOnTextChanged { text, _, _, _ ->
                viewModel.changeText(text.toString())
                currentText = text.toString()
            }

            binding.rabkinTestVoiceButton.setOnLongClickListener {
                showCommandsToast()
                true
            }

            rabkinTestButton.setOnClickListener {
                val finish = doAnswer()
                if (finish) {
                    navigateToResults()
                } else {
                    restartTest()
                }
            }

            rabkinTestButtonTriangle.setOnClickListener { appendToInput("треугольник") }
            rabkinTestButtonSquare.setOnClickListener { appendToInput("квадрат") }
            rabkinTestButtonCircle.setOnClickListener { appendToInput("круг") }

            rabkinTestQuestionButton.setOnClickListener {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            }

            rabkinTestOverlay.setOnClickListener {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
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

            rabkinTestMain.onSizeChange {
                if (!justStarted) {
                    SHOW_QUESTION = it
                    checkQuestionVisibility()
                }
            }
        }

        lifecycleScope.launch {
            delay(2000L)
            justStarted = false
        }
    }

    private fun updateButtonState() {
        binding.rabkinTestVoiceButton.setImageResource(
            if (VoiceAssistantManager.isRecording()) R.drawable.ic_mic_on
            else R.drawable.audio_mic_off_24
        )
    }

    private fun initializeFragment() {
        if (CURRENT_MEASURE == "rabkin") {
            setupRabkinTest()
        } else {
            setupSivtsevTest()
        }
        checkQuestionVisibility()
    }

    private fun setupRabkinTest() {
        binding.apply {
            rabkintTestBottomSheetText.setText(R.string.rabkin_guide_guide2)
            rabkinTestButtonCircle.visibility = View.VISIBLE
            rabkinTestButtonCircleText.visibility = View.VISIBLE
            rabkinTestButtonSquare.visibility = View.VISIBLE
            rabkinTestButtonSquareText.visibility = View.VISIBLE
            rabkinTestButtonTriangle.visibility = View.VISIBLE
            rabkinTestButtonTriangleText.visibility = View.VISIBLE
        }

        currentRabkinTest = viewModel.getRandomRabkinTest()
        loadImage("rabkin_test_${currentRabkinTest.chartNum}")
    }

    private fun setupSivtsevTest() {
        binding.apply {
            rabkintTestBottomSheetText.setText(R.string.sivtsev_guide_guide2)
            rabkinTestButtonCircle.visibility = View.GONE
            rabkinTestButtonCircleText.visibility = View.GONE
            rabkinTestButtonSquare.visibility = View.GONE
            rabkinTestButtonSquareText.visibility = View.GONE
            rabkinTestButtonTriangle.visibility = View.INVISIBLE
            rabkinTestButtonTriangleText.visibility = View.GONE
        }

        currentSivtsevTest = viewModel.getRandomSivtsevTest()
        loadImage("sivtsev_test_${currentSivtsevTest.testNum}")
    }

    private fun loadImage(imageName: String) {
        val id = resources.getIdentifier(imageName, "drawable", requireActivity().packageName)
        val drawablePic = resources.getDrawable(id)

        Glide.with(binding.rabkinTestImage)
            .load(drawablePic)
            .transform(RoundedCorners(40))
            .transition(DrawableTransitionOptions.withCrossFade(2000))
            .into(binding.rabkinTestImage)
    }

    private fun setupBottomSheet() {
        bottomSheetBehavior = BottomSheetBehavior.from(binding.rabkinTestBottomSheet).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                binding.rabkinTestOverlay.visibility = when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN, BottomSheetBehavior.STATE_COLLAPSED -> View.GONE
                    else -> View.VISIBLE
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })
    }

    private fun setupObservers() {
        viewModel.getLiveData().observe(viewLifecycleOwner) { state ->
            render(state)
        }
    }

    private fun render(state: RabkinTest) {
        binding.apply {
            rabkinTestInput.background = if (state.answer.isEmpty()) {
                requireActivity().getDrawable(R.drawable.rounded_corner_shape)
            } else {
                requireActivity().getDrawable(R.drawable.rounded_corner_shape_active)
            }

            rabkinTestButton.background = if (state.answer.isEmpty()) {
                requireActivity().getDrawable(R.drawable.arrow_forward_inactive)
            } else {
                requireActivity().getDrawable(R.drawable.arrow_forward_active)
            }

            rabkinTestButton.isEnabled = state.answer.isNotEmpty()
        }
    }

    private fun checkQuestionVisibility() {
        binding.rabkinTestQuestionButton.visibility = if (SHOW_QUESTION) View.VISIBLE else View.GONE
    }

    private fun doAnswer(): Boolean {
        return if (CURRENT_MEASURE == "rabkin") {
            viewModel.doAnswer(currentRabkinTest, currentText)
        } else {
            viewModel.doAnswer(currentSivtsevTest, currentText)
        }
    }

    private fun navigateToResults() {
        val navOptions = NavOptions.Builder()
            .setPopUpTo(findNavController().graph.startDestinationId, true)
            .build()
        findNavController().navigate(
            R.id.action_rabkinTestFragment_to_rabkinResultsFragment,
            null,
            navOptions
        )
    }

    private fun restartTest() {
        findNavController().navigate(R.id.action_rabkinTestFragment_self)
    }

    private fun appendToInput(text: String) {
        val current = binding.rabkinTestInput.text.toString()
        val newText = if (current.isEmpty()) text else "$current $text"
        binding.rabkinTestInput.setText(newText)
        currentText = newText
        binding.rabkinTestInput.setSelection(newText.length)
    }

    private fun showExitDialog() {
        MaterialAlertDialogBuilder(requireActivity(), R.style.MaterialAlertDialog2)
            .setTitle(getString(R.string.exit_title))
            .setMessage(getString(R.string.exit_descr))
            .setNeutralButton(R.string.no) { dialog, _ -> dialog.dismiss() }
            .setPositiveButton(R.string.yes) { _, _ ->
                val navOptions = NavOptions.Builder()
                    .setPopUpTo(findNavController().graph.startDestinationId, true)
                    .build()
                findNavController().navigate(
                    R.id.action_rabkinTestFragment_to_menuFragment,
                    null,
                    navOptions
                )
            }
            .show()
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

    private val availableCommands = listOf(
        "продолжить, вперед" to "Продолжить тестирование",
        "меню, команды" to "Показать список доступных команд",
        "назад" to "Завершить тест",
    )

    override fun onVoiceCommandRecognized(command: String) {
        activity?.runOnUiThread {
            when (command) {
                "продолжить" -> {
                    val finish = doAnswer()
                    if (finish) {
                        navigateToResults()
                    } else {
                        restartTest()
                    }
                }
                "вперед" -> {
                    val finish = doAnswer()
                    if (finish) {
                        navigateToResults()
                    } else {
                        restartTest()
                    }
                }
                "меню" -> {
                    showCommandsToast()
                }
                "команды" -> {
                    showCommandsToast()
                }
                "назад" -> showExitDialog()
                else -> Toast.makeText(context, "Команда '$command' не поддерживается", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onVoiceTextRecognized(text: String, type: String) {
        activity?.runOnUiThread {
            val convertedText = when (text) {
                "эн" -> "н"
                "эм" -> "м"
                "ка" -> "к"
                "ша" -> "ш"
                "бэ" -> "б"
                else -> text
            }
            appendToInput(convertedText)
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
        if (!prefs.getBoolean(COMMANDS_TOAST_SHOWN_KEY, false)) {
            showCommandsToast()
            prefs.edit().putBoolean(COMMANDS_TOAST_SHOWN_KEY, true).apply()
        }
    }

    override fun onPause() {
        super.onPause()
        VoiceAssistantManager.unregisterCallback()
    }

    companion object {
        private const val REQUEST_RECORD_AUDIO_PERMISSION = 200
    }

    private inline fun View?.onSizeChange(crossinline runnable: (Boolean) -> Unit) = this?.apply {
        addOnLayoutChangeListener { _, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
            val rect = Rect(left, top, right, bottom)
            val oldRect = Rect(oldLeft, oldTop, oldRight, oldBottom)
            runnable(rect.height() < oldRect.height())
        }
    }
}