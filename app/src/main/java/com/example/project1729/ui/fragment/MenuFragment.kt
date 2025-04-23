package com.example.project1729.ui.fragment

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.project1729.R
import com.example.project1729.data.keys.RABKIN_RESULTS.CURRENT_MEASURE
import com.example.project1729.databinding.FragmentMenuBinding
import com.example.project1729.voice.VoiceAssistant
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class MenuFragment : Fragment(), VoiceAssistant.VoiceCallback {

    private lateinit var binding: FragmentMenuBinding
    private lateinit var voiceAssistant: VoiceAssistant

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentMenuBinding.inflate(inflater, container, false)
        voiceAssistant = VoiceAssistant(requireContext(), this)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
    }

    private fun setupUI() {
        val sharedPrefs = requireActivity().getSharedPreferences(PROJECT1729_PREFERENCES, Application.MODE_PRIVATE)
        val userName = sharedPrefs.getString(USERNAME, "Leonid")
        binding.menuProfileName.text = userName
        binding.menuProfileImage.setOnClickListener {
            showExitDialog()
        }
        binding.menuProfileName.setOnClickListener {
            showExitDialog()
        }
        binding.menuProfileArrow.setOnClickListener {
            showExitDialog()
        }

        binding.menuProfileHistoryButton.setOnClickListener {
            findNavController().navigate(R.id.action_menuFragment_to_historyMainFragment)
        }

        binding.menuFirstItem.setOnClickListener {
            findNavController().navigate(R.id.action_menuFragment_to_rabkinGuideFragment)
            CURRENT_MEASURE = "rabkin"
        }
        binding.menuSecondItem.setOnClickListener {
            findNavController().navigate(R.id.action_menuFragment_to_rabkinGuideFragment)
            CURRENT_MEASURE = "sivtsev"
        }

        binding.menuVoiceButton.setOnClickListener {
            if (checkAudioPermission()) {
                voiceAssistant.toggleRecording()
            } else {
                requestAudioPermission()
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

    private fun showExitDialog(){
        MaterialAlertDialogBuilder(requireActivity(), R.style.MaterialAlertDialog2)
            .setTitle(getString(R.string.exit_account_title)) // Заголовок диалога
            .setMessage(getString(R.string.exit_account_descr)) // Описание диалога
            .setNeutralButton(R.string.exit_account_cancel) { dialog, which -> // Добавляет кнопку «Отмена»
                // Действия, выполняемые при нажатии на кнопку «Отмена»
            }

            .setPositiveButton(R.string.exit_account_confirm) { dialog, which -> // Добавляет кнопку «Да»
                val sharedPrefs = requireActivity().getSharedPreferences(PROJECT1729_PREFERENCES, Application.MODE_PRIVATE)
                sharedPrefs.edit()
                    .putString(USERNAME, NOT_AUTHORIZED)
                    .apply()

                val navOptions = NavOptions.Builder()
                    .setPopUpTo(findNavController().graph.startDestinationId, true) // Очищает весь back stack
                    .build()
                findNavController().navigate(R.id.action_menuFragment_to_startFragment, null, navOptions)
            }
            .show()
    }

    companion object{
        const val PROJECT1729_PREFERENCES = "PROJECT1729_PREFERENCES"
        const val USERNAME = "USERNAME"
        const val NOT_AUTHORIZED = "NOT_AUTHORIZED"
        private const val REQUEST_RECORD_AUDIO_PERMISSION = 200
    }

    override fun onVoiceCommandRecognized(command: String) {
        activity?.runOnUiThread {
            when (command) {
                "история" -> {
                    findNavController().navigate(R.id.action_menuFragment_to_historyMainFragment)
                }
                "цветоощущение" -> {
                    findNavController().navigate(R.id.action_menuFragment_to_rabkinGuideFragment)
                    CURRENT_MEASURE = "rabkin"
                }

                "первый тест" -> {
                    findNavController().navigate(R.id.action_menuFragment_to_rabkinGuideFragment)
                    CURRENT_MEASURE = "rabkin"
                }

                "цветовосприятие" -> {
                    findNavController().navigate(R.id.action_menuFragment_to_rabkinGuideFragment)
                    CURRENT_MEASURE = "rabkin"
                }

                "острота зрения" -> {
                    findNavController().navigate(R.id.action_menuFragment_to_rabkinGuideFragment)
                    CURRENT_MEASURE = "sivtsev"
                }

                "второй тест" -> {
                    findNavController().navigate(R.id.action_menuFragment_to_rabkinGuideFragment)
                    CURRENT_MEASURE = "sivtsev"
                }

                else -> Toast.makeText(
                    context,
                    "Команда '$command' не поддерживается",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onVoiceTextRecognized(text: String, type: String) {
        TODO("Not yet implemented")
    }

    override fun onError(error: String) {
        activity?.runOnUiThread {
            Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onRecordingStarted() {
        activity?.runOnUiThread {
            binding.menuVoiceButton.setImageResource(R.drawable.audio_mic_off_24)
            Toast.makeText(context, "Запись...", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onRecordingStopped() {
        activity?.runOnUiThread {
            binding.menuVoiceButton.setImageResource(R.drawable.ic_mic_on)
            Toast.makeText(context, "Обработка...", Toast.LENGTH_SHORT).show()
        }
    }

}