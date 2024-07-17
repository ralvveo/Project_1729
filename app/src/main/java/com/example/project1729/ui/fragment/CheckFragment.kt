package com.example.project1729.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.project1729.R
import com.example.project1729.databinding.FragmentCheckBinding
import com.example.project1729.ui.view_model.CheckViewModel
import com.example.project1729.utils.VoiceAssistant
import com.example.project1729.voiceAssistent.VoiceCommandHandler
import org.koin.androidx.viewmodel.ext.android.viewModel

class CheckFragment : Fragment(), VoiceCommandHandler {

    private lateinit var binding: FragmentCheckBinding
    private val viewModel by viewModel <CheckViewModel> ()
    private var voiceAssistant: VoiceAssistant? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentCheckBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.checkButtonStartCheck.setOnClickListener {
            findNavController().navigate(R.id.action_checkFragment_to_dopInfoFragment)
        }
        voiceAssistant = VoiceAssistant(requireContext(), findNavController(), this)

        binding.startButtonVoiceAssistant.setOnClickListener {
            Log.d("VoiceAssistant", "Ready for speech")

            if (voiceAssistant!!.isVoiceAssistantEnabled()) {
                voiceAssistant!!.stopListening()
                voiceAssistant!!.toggleVoiceAssistant()
                Log.d("VoiceAssistant", "stop")
            } else {
                voiceAssistant!!.startListening()
                voiceAssistant!!.toggleVoiceAssistant()
                Log.d("VoiceAssistant", "start")
            }
        }
    }
    override fun updateLoginField(login: String) {
        Log.d("VoiceAssistant", "Don't use this method")
    }

    override fun updatePasswordField(password: String) {
        Log.d("VoiceAssistant", "Don't use this method")
    }

    override fun updateDiagnosisField(diagnosis: String) {
        Log.d("VoiceAssistant", "Don't use this method")
    }

    override fun updateYearField(year: String) {
        Log.d("VoiceAssistant", "Don't use this method")
    }

    override fun updateFioField(fio: String) {
        Log.d("VoiceAssistant", "Don't use this method")
    }
}