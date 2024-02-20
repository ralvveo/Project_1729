package com.example.project1729

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.project1729.App.Companion.DARK_THEME_INDICATOR
import com.example.project1729.App.Companion.PLAYLIST_MAKER_PREFERENCES
import com.example.project1729.databinding.ActivityMeasureBinding
import com.example.project1729.databinding.ActivitySettingsBinding
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)



        //Переключатель темы
        val sharedPrefs = getSharedPreferences(PLAYLIST_MAKER_PREFERENCES, MODE_PRIVATE)
        binding.themeSwitcher.isChecked = sharedPrefs.getBoolean(DARK_THEME_INDICATOR, false)
        binding.themeSwitcher.setOnCheckedChangeListener { switcher, checked ->
            (applicationContext as App).switchTheme(checked)


        }
        binding.themeSwitcher.isChecked = sharedPrefs.getBoolean(DARK_THEME_INDICATOR, false)

    }


    override fun onResume() {
        super.onResume()
        val sharedPrefs = getSharedPreferences(PLAYLIST_MAKER_PREFERENCES, MODE_PRIVATE)
        binding.themeSwitcher.isChecked = sharedPrefs.getBoolean(DARK_THEME_INDICATOR, false)
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