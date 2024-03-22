package com.example.project1729.data

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.example.project1729.domain.ThemeSwitcherRepository

class ThemeSwitcherRepositoryImpl(val context: Context) : ThemeSwitcherRepository {

    override fun switchTheme()  {
        val sharedPrefs = context.getSharedPreferences(PROJECT_1729_PREFERENCES, Application.MODE_PRIVATE)
        val currentTheme = sharedPrefs.getBoolean(DARK_THEME_INDICATOR, false)
        sharedPrefs.edit()
            .putBoolean(DARK_THEME_INDICATOR, !currentTheme)
            .apply()
        AppCompatDelegate.setDefaultNightMode(
            if (currentTheme) {
                AppCompatDelegate.MODE_NIGHT_NO
            } else {
                AppCompatDelegate.MODE_NIGHT_YES
            }
        )
    }

    override fun setTheme(){
        val sharedPrefs = context.getSharedPreferences(PROJECT_1729_PREFERENCES, Application.MODE_PRIVATE)
        val currentTheme = sharedPrefs.getBoolean(DARK_THEME_INDICATOR, false)
        AppCompatDelegate.setDefaultNightMode(
            if (currentTheme) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

    override fun getTheme(): Boolean{
        val sharedPrefs = context.getSharedPreferences(PROJECT_1729_PREFERENCES, Application.MODE_PRIVATE)
        return sharedPrefs.getBoolean(DARK_THEME_INDICATOR, false)
    }

    companion object{
        const val PROJECT_1729_PREFERENCES = "project_1729_preferences"
        const val DARK_THEME_INDICATOR = "dark_theme_indicator"
    }

}