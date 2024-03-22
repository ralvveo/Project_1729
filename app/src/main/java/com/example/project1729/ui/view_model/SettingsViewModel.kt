package com.example.project1729.ui.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.project1729.creator.Creator

class SettingsViewModel : ViewModel() {

    private val themeSwitcher = Creator.provideThemeSwitcher()

    fun switchTheme(){
        themeSwitcher.switchTheme()
    }

    fun getTheme(): Boolean{
        return themeSwitcher.getTheme()
    }


    companion object {
        fun factory(): ViewModelProvider.Factory {
            return viewModelFactory {
                initializer {
                    SettingsViewModel()
                }
            }
        }
    }
}