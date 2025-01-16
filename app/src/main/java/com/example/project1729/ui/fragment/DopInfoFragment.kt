package com.example.project1729.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.project1729.R
import com.example.project1729.databinding.FragmentDopInfoBinding
import com.example.project1729.ui.view_model.DopInfoViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class DopInfoFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private lateinit var binding: FragmentDopInfoBinding
    private val viewModel by viewModel<DopInfoViewModel>()
    private var stress = "Нет нагрузки"
    private var stressDuration = "-"
    private var eyeTraining = "No"
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentDopInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
// Create an ArrayAdapter using the string array and a default spinner layout.
        ArrayAdapter.createFromResource(requireActivity(), R.array.stress_array, R.layout.color_spinner_layout).also { adapter ->
            // Specify the layout to use when the list of choices appears.
            adapter.setDropDownViewResource(R.layout.spinner_dropdown_layout)
            // Apply the adapter to the spinner.
            binding.dopInfoStressSpinner.adapter = adapter
        }

        binding.dopInfoStressSpinner.onItemSelectedListener = this

        binding.dopInfoStressDuration.doOnTextChanged { text, _, _, _ ->
            if (text?.isEmpty() == false) {
                stressDuration = text.toString()
                binding.dopInfoStressDuration.background = requireActivity().getDrawable(R.drawable.rounded_corner_shape_active)
                binding.dopInfoStressDurationActiveText.visibility = View.VISIBLE
            }
            else{
                stressDuration = "-"
                binding.dopInfoStressDuration.background = requireActivity().getDrawable(R.drawable.rounded_corner_shape)
                binding.dopInfoStressDurationActiveText.visibility = View.GONE
            }
        }

        binding.dopInfoButtonStart.setOnClickListener {
            viewModel.startCheck(stress, stressDuration, eyeTraining)
            findNavController().navigateUp()
        }

        binding.dopInfoRadio.setOnCheckedChangeListener { _, optionId ->
            run {
                when (optionId) {
                    R.id.dopInfoRadioYes -> {
                        eyeTraining = "Да"
                    }
                    R.id.dopInfoRadioNo -> {
                        eyeTraining = "Нет"
                    }

                }
            }
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        changeStressInfo(position)
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    private fun changeStressInfo(position: Int){
        when (position){
            0 -> {
                binding.dopInfoStressDuration.setText("")
                binding.dopInfoStressDuration.visibility = View.GONE
                binding.dopInfoStressDurationActiveText.visibility = View.GONE
                binding.dopInfoStressSpinner.background = requireActivity().getDrawable(R.drawable.rounded_corner_shape)
                binding.dopInfoStressDuration.background = requireActivity().getDrawable(R.drawable.rounded_corner_shape)
                binding.dopInfoStressActiveText.setTextColor(resources.getColor(R.color.black))
            }
            else -> {
                binding.dopInfoStressDuration.visibility = View.VISIBLE
                binding.dopInfoStressSpinner.background = requireActivity().getDrawable(R.drawable.rounded_corner_shape_active)
                binding.dopInfoStressActiveText.setTextColor(resources.getColor(R.color.blue))
            }
        }

        when(position){
            0 -> stress = "Нет нагрузки"
            1 -> stress = "Телефон"
            2 -> stress = "Компьютер"
            3 -> stress = "Планшет"
            4 -> stress = "Телевизор"
            5 -> stress = "Другое"
        }
    }

}