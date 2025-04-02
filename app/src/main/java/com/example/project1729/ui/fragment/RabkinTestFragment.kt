package com.example.project1729.ui.fragment

import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.widget.ConstraintLayout
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
import com.example.project1729.data.keys.RABKIN_RESULTS.RABKIN_DEITERANOPY_ANSWERS
import com.example.project1729.data.keys.RABKIN_RESULTS.RABKIN_PROTANOPY_ANSWERS
import com.example.project1729.data.keys.RABKIN_RESULTS.RABKIN_RIGHT_ANSWERS
import com.example.project1729.data.keys.RABKIN_RESULTS.RABKIN_WRONG_ANSWERS
import com.example.project1729.data.keys.RABKIN_RESULTS.SHOW_QUESTION
import com.example.project1729.databinding.FragmentRabkinTestBinding
import com.example.project1729.domain.model.RabkinTest
import com.example.project1729.domain.model.SivtsevTest
import com.example.project1729.ui.view_model.RabkinTestViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class RabkinTestFragment : Fragment() {

    lateinit var binding: FragmentRabkinTestBinding
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private val viewModel by viewModel<RabkinTestViewModel>()
    var justStarted = true
    private var currentText = ""
    private var currentRabkinTest = RabkinTest("", "", "", "")
    private var currentSivtsevTest = SivtsevTest("", "", "")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentRabkinTestBinding.inflate(inflater, container, false)


        binding.rabkinTestButtonBack.setOnClickListener {
            showExitDialog()
        }

        initializeFragment()

        binding.rabkinTestInput.requestFocus()
        binding.rabkinTestInput.doOnTextChanged { text, _, _, _ ->
            viewModel.changeText(text.toString())
            currentText = text.toString()
        }

        checkQuestion()
        viewModel.getLiveData().observe(viewLifecycleOwner) { state ->
            render(state)
        }

        binding.rabkinTestMain.onSizeChange {

        }
        lifecycleScope.launch{
            delay(2000L)
            justStarted = false
        }



        binding.rabkinTestQuestionButton.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
        binding.rabkinTestOverlay.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }

        binding.rabkinTestButtonTriangle.setOnClickListener {
            if (binding.rabkinTestInput.text.isEmpty()){
                binding.rabkinTestInput.setText("треугольник ")

            }
            else{
                binding.rabkinTestInput.append("треугольник")
            }
            currentText = binding.rabkinTestInput.text.toString()
            binding.rabkinTestInput.setSelection(binding.rabkinTestInput.length())//placing cursor at the end of the text
        }


        binding.rabkinTestButtonSquare.setOnClickListener {
            if (binding.rabkinTestInput.text.isEmpty()){
                binding.rabkinTestInput.setText("квадрат ")
            }
            else{
                binding.rabkinTestInput.append("квадрат")

            }
            currentText = binding.rabkinTestInput.text.toString()
            binding.rabkinTestInput.setSelection(binding.rabkinTestInput.length())//placing cursor at the end of the text
        }

        binding.rabkinTestButtonCircle.setOnClickListener {
            if (binding.rabkinTestInput.text.isEmpty()){
                binding.rabkinTestInput.setText("круг ")
            }

            else{
                binding.rabkinTestInput.append("круг")

            }
            currentText = binding.rabkinTestInput.text.toString()
            binding.rabkinTestInput.setSelection(binding.rabkinTestInput.length())//placing cursor at the end of the text
        }


        binding.rabkinTestButton.setOnClickListener {
            val finish = doAnswer()
            if (finish){
                val navOptions = NavOptions.Builder()
                    .setPopUpTo(findNavController().graph.startDestinationId, true) // Очищает весь back stack
                    .build()
                findNavController().navigate(R.id.action_rabkinTestFragment_to_rabkinResultsFragment, null, navOptions)
            }
            else{
                findNavController().navigate(R.id.action_rabkinTestFragment_self)
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(backPressedCallback)
        backPressedCallback.isEnabled = true



        return binding.root
    }
    fun checkQuestion(){
        if (SHOW_QUESTION){
            binding.rabkinTestQuestionButton.visibility = View.VISIBLE
        }
        else{
            binding.rabkinTestQuestionButton.visibility = View.GONE
        }
    }

    private fun initializeFragment() {

        if (CURRENT_MEASURE == "rabkin"){
            binding.rabkintTestBottomSheetText.setText(R.string.rabkin_guide_guide2)
            binding.rabkinTestButtonCircle.visibility = View.VISIBLE
            binding.rabkinTestButtonCircleText.visibility = View.VISIBLE
            binding.rabkinTestButtonSquare.visibility = View.VISIBLE
            binding.rabkinTestButtonSquareText.visibility = View.VISIBLE
            binding.rabkinTestButtonTriangle.visibility = View.VISIBLE
            binding.rabkinTestButtonTriangleText.visibility = View.VISIBLE

            currentRabkinTest = viewModel.getRandomRabkinTest()

            val pictureName = "rabkin_test_${currentRabkinTest.chartNum}"
            val id = resources.getIdentifier(pictureName, "drawable", requireActivity().packageName)
            val drawablePic = resources.getDrawable(id)

            Glide.with(binding.rabkinTestImage)
                .load(drawablePic)
                .transform(RoundedCorners(40))
                .transition(DrawableTransitionOptions.withCrossFade(2000))
                .into(binding.rabkinTestImage)
        }
        else{

            currentSivtsevTest = viewModel.getRandomSivtsevTest()
            val pictureName = "sivtsev_test_${currentSivtsevTest.testNum}"
            val id = resources.getIdentifier(pictureName, "drawable", requireActivity().packageName)
            val drawablePic = resources.getDrawable(id)
            Glide.with(binding.rabkinTestImage)
                .load(drawablePic)
                .transform(RoundedCorners(40))
                .transition(DrawableTransitionOptions.withCrossFade(2000))
                .into(binding.rabkinTestImage)

            binding.rabkintTestBottomSheetText.setText(R.string.sivtsev_guide_guide2)
            binding.rabkinTestButtonCircle.visibility = View.GONE
            binding.rabkinTestButtonCircleText.visibility = View.GONE
            binding.rabkinTestButtonSquare.visibility = View.GONE
            binding.rabkinTestButtonSquareText.visibility = View.GONE
            binding.rabkinTestButtonTriangle.visibility = View.INVISIBLE
            binding.rabkinTestButtonTriangleText.visibility = View.GONE
        }

        bottomSheetBehavior = BottomSheetBehavior.from(binding.rabkinTestBottomSheet).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        bottomSheetBehavior.addBottomSheetCallback(object :

            BottomSheetBehavior.BottomSheetCallback() {

            override fun onStateChanged(bottomSheet: View, newState: Int) {

                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN, BottomSheetBehavior.STATE_COLLAPSED -> {
                        binding.rabkinTestOverlay.visibility = View.GONE
                    }

                    else -> {
                        binding.rabkinTestOverlay.visibility = View.VISIBLE
                    }
                }
            }
            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }
        })

    }

    private fun render(state: RabkinTest){
        if (state.answer == ""){
            binding.rabkinTestInput.background = requireActivity().getDrawable(R.drawable.rounded_corner_shape)
            binding.rabkinTestButton.background =  requireActivity().getDrawable(R.drawable.arrow_forward_inactive)
            binding.rabkinTestButton.isEnabled = false

        }
        else {
            binding.rabkinTestInput.background = requireActivity().getDrawable(R.drawable.rounded_corner_shape_active)
            binding.rabkinTestButton.background =  requireActivity().getDrawable(R.drawable.arrow_forward_active)
            binding.rabkinTestButton.isEnabled = true
        }
    }


    private fun doAnswer(): Boolean{
        if (CURRENT_MEASURE == "rabkin"){
            return viewModel.doAnswer(currentRabkinTest, currentText)
        }
        else{
            return viewModel.doAnswer(currentSivtsevTest, currentText)
        }
    }

    private val backPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            showExitDialog()
        }
    }

    inline fun View?.onSizeChange(crossinline runnable: () -> Unit) = this?.apply {
        addOnLayoutChangeListener { _, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
            val rect = Rect(left, top, right, bottom)
            val oldRect = Rect(oldLeft, oldTop, oldRight, oldBottom)
            if (rect.height() < oldRect.height()) {
                SHOW_QUESTION = false
                checkQuestion()
            }
            else{
                if (!justStarted){
                    SHOW_QUESTION = true
                    checkQuestion()
                }

            }

            Log.d("SHOWQUEST", "$SHOW_QUESTION")
        }
    }

    private fun showExitDialog(){
        MaterialAlertDialogBuilder(requireActivity(), R.style.MaterialAlertDialog2)
            .setTitle(getString(R.string.exit_title)) // Заголовок диалога
            .setMessage(getString(R.string.exit_descr)) // Описание диалога
            .setNeutralButton(R.string.no) { dialog, which -> // Добавляет кнопку «Отмена»
                // Действия, выполняемые при нажатии на кнопку «Отмена»
            }

            .setPositiveButton(R.string.yes) { dialog, which -> // Добавляет кнопку «Да»
                val navOptions = NavOptions.Builder()
                    .setPopUpTo(findNavController().graph.startDestinationId, true) // Очищает весь back stack
                    .build()
                findNavController().navigate(R.id.action_rabkinTestFragment_to_menuFragment, null, navOptions)
            }
            .show()
    }

    override fun onStop() {
        super.onStop()
        backPressedCallback.isEnabled = false
    }

}