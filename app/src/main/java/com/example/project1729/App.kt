package com.example.project1729

import android.app.Application
import android.bluetooth.BluetoothManager
import android.content.Context
import androidx.core.content.ContextCompat.getSystemService
import com.example.project1729.bt.bluetooth.BluetoothController
import com.example.project1729.creator.Creator

class App : Application() {

    companion object{
        var bluetoothConnected = false
        lateinit var bluetoothController: BluetoothController
    }

    override fun onCreate(){
        Creator.init(this)
        val bManager = this.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        val btAdapter = bManager.adapter
        bluetoothController = Creator.provideBluetoothController(btAdapter)
        super.onCreate()
    }




}