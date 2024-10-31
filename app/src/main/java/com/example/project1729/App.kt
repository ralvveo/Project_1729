package com.example.project1729

import android.app.Application
import android.bluetooth.BluetoothManager
import android.content.Context
import com.example.project1729.bt.bluetooth.BluetoothController
import com.example.project1729.di.appModule
import com.example.project1729.di.dataModule
import com.example.project1729.di.repositoryModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    companion object{
        var bluetoothConnected = false
        var inputMessages = ""
        var resultKCHSM = ""
        var resultTemp = ""
        var resultPressure = ""
        lateinit var bluetoothController: BluetoothController
        var currentMessagesLength = 0
        val MAIN_URL = "https://script.google.com/macros/s/AKfycbwEiSKLnvtF1kZ1UH2__J-y2H0FCh9LF8zsXRAqNw4y54p4IBdV2UvjnvrOz5L_Zz0S/exec"
        val MAIN_URL2 = "https://script.google.com/macros/s/AKfycbwEiSKLnvtF1kZ1UH2__J-y2H0FCh9LF8zsXRAqNw4y54p4IBdV2UvjnvrOz5L_Zz0S/"
    }

    override fun onCreate(){
        val bManager = this.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        val btAdapter = bManager.adapter
        bluetoothController = BluetoothController(this, btAdapter)
        super.onCreate()

        startKoin{
            androidContext(this@App)
            modules(appModule, repositoryModule, dataModule)
        }
    }




}