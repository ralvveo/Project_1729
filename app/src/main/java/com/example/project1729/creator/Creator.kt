package com.example.project1729.creator

import com.example.project1729._unsorted.basic.App
import com.example.project1729.data.ThemeSwitcherRepositoryImpl
import com.example.project1729.domain.ThemeSwitcherRepository

object Creator {

    private lateinit var applicationContext: App
    fun init (newApplicationContext: App){
        applicationContext = newApplicationContext
    }


    fun provideThemeSwitcher(): ThemeSwitcherRepository{
        return ThemeSwitcherRepositoryImpl(applicationContext)
    }
}