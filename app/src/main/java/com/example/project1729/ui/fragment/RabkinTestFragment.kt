package com.example.project1729.ui.fragment

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

    private fun setupUI() {
        binding.apply {
            rabkinTestButtonBack.setOnClickListener { showExitDialog() }
            rabkinTestInput.requestFocus()

            rabkinTestInput.doOnTextChanged { text, _, _, _ ->
                viewModel.changeText(text.toString())
                currentText = text.toString()
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

            rabkinTestVoiceButton.setOnClickListener {
                if (checkAudioPermission()) {
                    voiceAssistant.toggleRecording()
                } else {
                    requestAudioPermission()
                }
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

    override fun onRecordingStarted() {
        activity?.runOnUiThread {
            binding.rabkinTestVoiceButton.setImageResource(R.drawable.audio_mic_off_24)
            Toast.makeText(context, "Запись...", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onRecordingStopped() {
        activity?.runOnUiThread {
            binding.rabkinTestVoiceButton.setImageResource(R.drawable.ic_mic_on)
            Toast.makeText(context, "Обработка...", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onStop() {
        super.onStop()
        backPressedCallback.isEnabled = false
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