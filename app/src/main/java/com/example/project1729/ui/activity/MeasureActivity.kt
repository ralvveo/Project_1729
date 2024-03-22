package com.example.project1729.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.project1729.databinding.ActivityMeasureBinding

class MeasureActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMeasureBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMeasureBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }

}