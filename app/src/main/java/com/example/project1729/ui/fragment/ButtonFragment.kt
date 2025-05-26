package com.example.project1729.ui.fragment

import android.app.Application
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.project1729.R
import com.example.project1729.data.repository.NetworkRepositoryImpl.Companion.PROJECT1729_PREFERENCES
import com.example.project1729.databinding.FragmentButtonBinding
import com.example.project1729.domain.model.KchsmMeasure
import com.example.project1729.ui.view_model.KchsmViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.getValue


class ButtonFragment : Fragment() {

    private val viewModel by viewModel<KchsmViewModel>()
    private lateinit var binding: FragmentButtonBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentButtonBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            delay(5000L)
            render()
        }
        binding.bigRoundButton.setOnClickListener {
            viewModel.sendMessage("STOP")
            lifecycleScope.launch {
                delay(1000L)
                findNavController().navigateUp()
            }
        }


    }

    private fun render() {
        val sharedPrefs = requireActivity().getSharedPreferences(
            PROJECT1729_PREFERENCES,
            Application.MODE_PRIVATE
        )
        val temperature = truncateAfterDot(sharedPrefs.getString("temperature", "--")?: "24")
        val humidity = truncateAfterDot(sharedPrefs.getString("humidity", "--") ?: "41")
        val lux = truncateAfterDot(sharedPrefs.getString("lux", "--") ?: "74")
        binding.temperatureValue.text = "$temperature °C"
        binding.humidityValue.text = "$humidity %"
        binding.luxValue.text = "$lux лк"
    }

    private fun truncateAfterDot(input: String): String {
        val dotIndex = input.indexOf('.')
        return if (dotIndex != -1) {
            input.substring(0, dotIndex)
        } else {
            input
        }
    }
}



