package com.example.project1729._unsorted.basic

import android.app.Application
import com.example.project1729._unsorted.bt.bluetooth.BluetoothController
import com.example.project1729.creator.Creator

class App : Application() {

    companion object{
        var bluetoothConnected = false
    }


    override fun onCreate(){
        Creator.init(this)
        BluetoothController.init(this)
        super.onCreate()
    }




}