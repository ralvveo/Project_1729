package com.example.project1729.ui.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.project1729.databinding.FragmentSettingsBinding
import com.example.project1729.ui.view_model.SettingsViewModel

class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding
    private lateinit var viewModel: SettingsViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSettingsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity(), SettingsViewModel.factory())[SettingsViewModel::class.java]
        binding.themeSwitcher.isChecked = viewModel.getTheme()
        //Переключатель темы
        binding.themeSwitcher.setOnCheckedChangeListener { switcher, checked ->
            viewModel.switchTheme()
        }
    }

    }

//    //Фикс бага с мигающим экраном при смене темы
//    override fun recreate() {
//        finish()
//        overridePendingTransition(
//            R.anim.empty_animation,
//            R.anim.empty_animation
//        )
//        startActivity(intent)
//        overridePendingTransition(
//            R.anim.empty_animation,
//            R.anim.empty_animation
//        )
//    }
//
//
//}