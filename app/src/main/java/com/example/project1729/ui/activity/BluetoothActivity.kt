package com.example.project1729.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.project1729.databinding.ActivityBluetoothBinding

class BluetoothActivity : AppCompatActivity() {


    private lateinit var binding: ActivityBluetoothBinding




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBluetoothBinding.inflate(layoutInflater)
        setContentView(binding.root)



    }

}