package com.example.project1729.creator

import android.bluetooth.BluetoothAdapter
import com.example.project1729.App
import com.example.project1729.bt.bluetooth.BluetoothController
import com.example.project1729.data.repository.HistoryUpdaterRepositoryImpl
import com.example.project1729.data.repository.ThemeSwitcherRepositoryImpl
import com.example.project1729.domain.repository.HistoryUpdaterRepository
import com.example.project1729.domain.repository.ThemeSwitcherRepository

object Creator {

    private lateinit var applicationContext: App
    fun init (newApplicationContext: App){
        applicationContext = newApplicationContext
    }

    fun provideHistoryUpdater(): HistoryUpdaterRepository{
        return HistoryUpdaterRepositoryImpl(context = applicationContext)
    }


    fun provideBluetoothController(adapter: BluetoothAdapter): BluetoothController {
        return BluetoothController(context = applicationContext, adapter = adapter)
    }

    fun provideThemeSwitcher(): ThemeSwitcherRepository {
        return ThemeSwitcherRepositoryImpl(applicationContext)
    }
}