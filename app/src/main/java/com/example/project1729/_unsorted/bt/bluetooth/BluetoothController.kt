package com.example.project1729._unsorted.bt.bluetooth

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import com.example.bt.bluetooth.ConnectThread
import com.example.project1729._unsorted.basic.App

object BluetoothController {

    private lateinit var applicationContext: App
    private var connectThread: ConnectThread? = null
    private lateinit var btAdapter: BluetoothAdapter

    fun init (newApplicationContext: App){
        applicationContext = newApplicationContext
        val bManager = applicationContext.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        btAdapter = bManager.adapter
    }

    fun connect(mac: String, listener: Listener) {
        if(btAdapter.isEnabled && mac.isNotEmpty()) {
            val device = btAdapter.getRemoteDevice(mac)
            connectThread = ConnectThread(device, listener)
            connectThread?.start()
        }
    }

    fun sendMessage(message: String){
        connectThread?.sendMessage(message)
    }

    fun closeConnection(){
        connectThread?.closeConnection()
    }

    interface Listener{
        fun onReceive(message: String)
    }
}