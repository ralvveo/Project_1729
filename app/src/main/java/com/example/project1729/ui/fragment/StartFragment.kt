package com.example.project1729.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.project1729.R
import com.example.project1729.databinding.FragmentStartBinding
import com.example.project1729.utils.VoiceAssistant
import com.example.project1729.voiceAssistent.VoiceCommandHandler

class StartFragment : Fragment(), VoiceCommandHandler {

    private lateinit var binding: FragmentStartBinding
    private var voiceAssistant: VoiceAssistant? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentStartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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

        binding.startButtonLogin.setOnClickListener {
            findNavController().navigate(R.id.action_startFragment_to_loginFragment)
        }

        binding.startButtonRegister.setOnClickListener {
            findNavController().navigate(R.id.action_startFragment_to_registerFragment)
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