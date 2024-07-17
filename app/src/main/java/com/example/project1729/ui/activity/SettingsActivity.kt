package com.example.project1729.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.project1729.R
import com.example.project1729.databinding.ActivitySettingsBinding
import com.example.project1729.ui.view_model.MainViewModel
import com.example.project1729.ui.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private val viewModel by viewModel <SettingsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.themeSwitcher.isChecked = viewModel.getTheme()
        //Переключатель темы
        binding.themeSwitcher.setOnCheckedChangeListener { switcher, checked ->
            viewModel.switchTheme()
        }
    }

    //Фикс бага с мигающим экраном при смене темы
    override fun recreate() {
        finish()
        overridePendingTransition(
            R.anim.empty_animation,
            R.anim.empty_animation
        )
        startActivity(intent)
        overridePendingTransition(
            R.anim.empty_animation,
            R.anim.empty_animation
        )
    }


}