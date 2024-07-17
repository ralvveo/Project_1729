package com.example.project1729.ui.view_model

import androidx.lifecycle.ViewModel
import com.example.project1729.domain.repository.ThemeSwitcherRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SettingsViewModel : ViewModel(), KoinComponent {

    private val themeSwitcher: ThemeSwitcherRepository by inject()

    fun switchTheme(){
        themeSwitcher.switchTheme()
    }

    fun getTheme(): Boolean{
        return themeSwitcher.getTheme()
    }


}