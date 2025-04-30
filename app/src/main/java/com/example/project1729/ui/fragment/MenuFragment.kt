package com.example.project1729.ui.fragment

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.project1729.R
import com.example.project1729.data.keys.RABKIN_RESULTS.CURRENT_MEASURE
import com.example.project1729.databinding.FragmentMenuBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class MenuFragment : Fragment() {

    private lateinit var binding: FragmentMenuBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMenuBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sharedPrefs = requireActivity().getSharedPreferences(
            PROJECT1729_PREFERENCES,
            Application.MODE_PRIVATE
        )
        val userName = sharedPrefs.getString(USERNAME, "Local_user")
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
        binding.menuFirstItem.setOnClickListener {
            findNavController().navigate(R.id.action_menuFragment_to_rabkinGuideFragment)
            CURRENT_MEASURE = "rabkin"
        }

        binding.menuSecondItem.setOnClickListener {
            findNavController().navigate(R.id.action_menuFragment_to_rabkinGuideFragment)
            CURRENT_MEASURE = "sivtsev"
        }

        binding.menuProfileHistoryButton.setOnClickListener {
            findNavController().navigate(R.id.action_menuFragment_to_historyMainFragment)
        }
    }

    private fun showExitDialog() {
        MaterialAlertDialogBuilder(requireActivity(), R.style.MaterialAlertDialog2)
            .setTitle(getString(R.string.exit_account_title)) // Заголовок диалога
            .setMessage(getString(R.string.exit_account_descr)) // Описание диалога
            .setNeutralButton(R.string.exit_account_cancel) { dialog, which -> // Добавляет кнопку «Отмена»
                // Действия, выполняемые при нажатии на кнопку «Отмена»
            }

            .setPositiveButton(R.string.exit_account_confirm) { dialog, which -> // Добавляет кнопку «Да»
                val sharedPrefs = requireActivity().getSharedPreferences(
                    PROJECT1729_PREFERENCES,
                    Application.MODE_PRIVATE
                )
                sharedPrefs.edit()
                    .putString(USERNAME, NOT_AUTHORIZED)
                    .apply()

                val navOptions = NavOptions.Builder()
                    .setPopUpTo(
                        findNavController().graph.startDestinationId,
                        true
                    ) // Очищает весь back stack
                    .build()
                findNavController().navigate(
                    R.id.action_menuFragment_to_startFragment,
                    null,
                    navOptions
                )
            }
            .show()
    }

    companion object {
        const val PROJECT1729_PREFERENCES = "PROJECT1729_PREFERENCES"
        const val USERNAME = "USERNAME"
        const val USERID = "USERID"
        const val NOT_AUTHORIZED = "NOT_AUTHORIZED"
    }

}