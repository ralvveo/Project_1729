package com.example.project1729.bt.bluetooth

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import com.example.bt.bluetooth.ConnectThread
import com.example.project1729.App
import com.example.project1729.bt.basic.BluetoothConstants

class BluetoothController(context: Context, private val adapter: BluetoothAdapter) {
    private var connectThread: ConnectThread? = null

    private val preference = context.getSharedPreferences(BluetoothConstants.PREFERENCE, Context.MODE_PRIVATE)
    private val mac = preference?.getString(BluetoothConstants.MAC, "")

    fun connect(listener: Listener) {
        if(adapter.isEnabled && !mac.isNullOrEmpty()) {
            val device = adapter.getRemoteDevice(mac)
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