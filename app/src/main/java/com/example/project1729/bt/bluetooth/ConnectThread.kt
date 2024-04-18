package com.example.bt.bluetooth

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import com.example.project1729.bt.bluetooth.BluetoothController
import java.io.IOException
import java.util.UUID

class ConnectThread(device: BluetoothDevice, val listener: BluetoothController.Listener) :
    Thread() {
    private val uuid = "00001101-0000-1000-8000-00805F9B34FB"
    private var mSocket: BluetoothSocket? = null

    init {
        try {
            mSocket = device.createRfcommSocketToServiceRecord(UUID.fromString(uuid))
        } catch (e: IOException) {

        } catch (se: SecurityException) {

        }
    }

    override fun run() {
        try {
            mSocket?.connect()
            listener.onReceive("bluetooth connected")
            readMessage()
        } catch (e: IOException) {
            listener.onReceive("bluetooth not connected")
        } catch (se: SecurityException) {

        }
    }

    private fun readMessage() {
        val buffer = ByteArray(256)
        while (true) {
            try {
                val length = mSocket?.inputStream?.read(buffer)
                val message = String(buffer, 0, length ?: 0)
                listener.onReceive(message)
            } catch (e: IOException) {
                listener.onReceive("bluetooth not connected")
                break
            }
        }
    }

    fun sendMessage(message: String){
        try {
            mSocket?.outputStream?.write(message.toByteArray())
        }catch (e: IOException){

        }
    }

    fun closeConnection() {
        try {
            mSocket?.close()
            listener.onReceive("bluetooth not connected")
        } catch (e: IOException) {
        }
    }
}