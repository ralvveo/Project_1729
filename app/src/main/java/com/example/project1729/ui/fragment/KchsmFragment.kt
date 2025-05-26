package com.example.project1729.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.project1729.R
import com.example.project1729.databinding.FragmentKchsmBinding
import com.example.project1729.domain.model.KchsmMeasure
import com.example.project1729.ui.view_model.KchsmViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class KchsmFragment : Fragment() {

    private lateinit var binding: FragmentKchsmBinding
    private var eyeState = STATE_RIGHT
    private var connectState = STATE_UNCONNECTED
    private var buttonState = STATE_RED
    private val viewModel by viewModel<KchsmViewModel>()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentKchsmBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
            if (connectState == STATE_UNCONNECTED){
                viewModel.connect()
            }
            else{
                var currentEye = "L"
                var currentColor = "R"
                findNavController().navigate(R.id.action_kchsmFragment_to_buttonFragment)
                if (eyeState == STATE_LEFT){
                    currentEye = "L"
                }
                else{
                    currentEye = "R"
                }
                if (eyeState == STATE_RED){
                    currentColor = "R"
                }
                else if (eyeState == STATE_GREEN){
                    currentColor = "G"
                }
                else{
                    currentColor = "B"
                }
                viewModel.sendMessage("D$currentColor$currentEye")
            }

        }

        viewModel.getDeviceLiveData().observe(viewLifecycleOwner) {
            message -> render(message)
        }
        viewModel.getResultLiveData().observe(viewLifecycleOwner){
            kchsmMeasure -> render(kchsmMeasure)
        }




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


    // ДОДЕЛАТЬ ПОДКЛЮЧЕНИЕ

    companion object{
        private const val STATE_LEFT = "state_left"
        private const val STATE_RIGHT = "state_right"
        private const val STATE_CONNECTED = "state_connected"
        private const val STATE_UNCONNECTED = "state_unconnected"
        private const val STATE_RED = "state_red"
        private const val STATE_BLUE = "state_blue"
        private const val STATE_GREEN = "state_green"

    }
}

