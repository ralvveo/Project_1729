package com.example.project1729.ui.fragment

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.project1729.R
import com.example.project1729.databinding.FragmentHistoryMainBinding
import com.example.project1729.voice.VoiceAssistant


class HistoryMainFragment : Fragment(), VoiceAssistant.VoiceCallback {

    private lateinit var binding: FragmentHistoryMainBinding
    private lateinit var voiceAssistant: VoiceAssistant

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentHistoryMainBinding.inflate(inflater, container, false)
        voiceAssistant = VoiceAssistant(requireContext(), this)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()

//        binding.historyMainThirdItem.setOnClickListener {
//            findNavController().navigate(
//                R.id.action_historyMainFragment_to_historyContentFragment,
//                HistoryContentFragment.createArgs(HISTORY_CONTENT_KCHSM)
//            )
//        }
//
//        binding.historyMainFourthItem.setOnClickListener {
//            findNavController().navigate(
//                R.id.action_historyMainFragment_to_historyContentFragment,
//                HistoryContentFragment.createArgs(HISTORY_CONTENT_KCHSM)
//            )
//        }
    }


    private fun setupUI() {

        binding.historyMainButtonBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.historyMainFirstItem.setOnClickListener {
            findNavController().navigate(
                R.id.action_historyMainFragment_to_historyContentFragment,
                HistoryContentFragment.createArgs(HISTORY_CONTENT_RABKIN)
            )
        }

        binding.historyMainSecondItem.setOnClickListener {
            findNavController().navigate(
                R.id.action_historyMainFragment_to_historyContentFragment,
                HistoryContentFragment.createArgs(HISTORY_CONTENT_SIVTSEV)
            )
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

    companion object{
        const val HISTORY_CONTENT_RABKIN = "history_content_rabkin"
        const val HISTORY_CONTENT_SIVTSEV = "history_content_sivtsev"
        const val HISTORY_CONTENT_KCHSM = "history_content_KCHSM"
        const val HISTORY_CONTENT_PHOTO = "history_content_photo"
        private const val REQUEST_RECORD_AUDIO_PERMISSION = 200
    }

    override fun onVoiceCommandRecognized(command: String) {
        activity?.runOnUiThread {
            when (command) {
                "назад" -> {
                    findNavController().navigateUp()
                }
                "цветоощущение" -> {
                    findNavController().navigate(
                        R.id.action_historyMainFragment_to_historyContentFragment,
                        HistoryContentFragment.createArgs(HISTORY_CONTENT_RABKIN)
                    )
                }

                "первый тест" -> {
                    findNavController().navigate(
                        R.id.action_historyMainFragment_to_historyContentFragment,
                        HistoryContentFragment.createArgs(HISTORY_CONTENT_RABKIN)
                    )
                }

                "цветовосприятие" -> {
                    findNavController().navigate(
                        R.id.action_historyMainFragment_to_historyContentFragment,
                        HistoryContentFragment.createArgs(HISTORY_CONTENT_RABKIN)
                    )
                }

                "острота зрения" -> {
                    findNavController().navigate(
                        R.id.action_historyMainFragment_to_historyContentFragment,
                        HistoryContentFragment.createArgs(HISTORY_CONTENT_SIVTSEV)
                    )
                }

                "второй тест" -> {
                    findNavController().navigate(
                        R.id.action_historyMainFragment_to_historyContentFragment,
                        HistoryContentFragment.createArgs(HISTORY_CONTENT_SIVTSEV)
                    )
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