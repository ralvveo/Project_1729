package com.example.project1729

import android.annotation.SuppressLint
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ScrollView
import com.example.project1729.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.testingEyeButton1.setOnClickListener {
            testingEyeButtonPressed(1)
        }

        binding.testingEyeButton2.setOnClickListener {
            testingEyeButtonPressed(2)
        }


        binding.measurementMethodButton1.setOnClickListener {
            measurementMethodButtonPressed(1)
        }

        binding.measurementMethodButton2.setOnClickListener {
            measurementMethodButtonPressed(2)
        }

        binding.measurementMethodButton3.setOnClickListener {
            measurementMethodButtonPressed(3)
        }


        binding.radiationIntensityButton1.setOnClickListener {
            radiationIntensityButtonPressed(1)
        }

        binding.radiationIntensityButton2.setOnClickListener {
            radiationIntensityButtonPressed(2)
        }

        binding.radiationIntensityButton3.setOnClickListener {
            radiationIntensityButtonPressed(3)
        }

        binding.redButton.setOnClickListener {
            diodeColorChanged(1)
        }

        binding.greenButton.setOnClickListener {
            diodeColorChanged(2)
        }

        binding.blueButton.setOnClickListener {
            diodeColorChanged(3)
        }

        binding.whiteButton.setOnClickListener {
            diodeColorChanged(4)
        }
    }


    fun testingEyeButtonPressed(btnNum: Int){
        val btn1 = binding.testingEyeButton1
        val btn2 = binding.testingEyeButton2
        when (btnNum) {
            1 -> {
                btn1.setBackgroundColor(getResources().getColor(R.color.white))
                btn2.setBackgroundColor(getResources().getColor(R.color.grey1))

            }
            2 -> {
                btn1.setBackgroundColor(getResources().getColor(R.color.grey1))
                btn2.setBackgroundColor(getResources().getColor(R.color.white))

            }
        }
    }

    fun measurementMethodButtonPressed(btnNum: Int){
        val btn1 = binding.measurementMethodButton1
        val btn2 = binding.measurementMethodButton2
        val btn3 = binding.measurementMethodButton3

        when (btnNum) {
            1 -> {
                btn1.setBackgroundColor(getResources().getColor(R.color.white))
                btn2.setBackgroundColor(getResources().getColor(R.color.grey1))
                btn3.setBackgroundColor(getResources().getColor(R.color.grey1))

            }
            2 -> {
                btn1.setBackgroundColor(getResources().getColor(R.color.grey1))
                btn2.setBackgroundColor(getResources().getColor(R.color.white))
                btn3.setBackgroundColor(getResources().getColor(R.color.grey1))

            }

            3 -> {
                btn1.setBackgroundColor(getResources().getColor(R.color.grey1))
                btn2.setBackgroundColor(getResources().getColor(R.color.grey1))
                btn3.setBackgroundColor(getResources().getColor(R.color.white))

            }
        }
    }

    fun radiationIntensityButtonPressed(btnNum: Int){
        val btn1 = binding.radiationIntensityButton1
        val btn2 = binding.radiationIntensityButton2
        val btn3 = binding.radiationIntensityButton3

        when (btnNum) {
            1 -> {
                btn1.setBackgroundColor(getResources().getColor(R.color.white))
                btn2.setBackgroundColor(getResources().getColor(R.color.grey1))
                btn3.setBackgroundColor(getResources().getColor(R.color.grey1))

            }
            2 -> {
                btn1.setBackgroundColor(getResources().getColor(R.color.grey1))
                btn2.setBackgroundColor(getResources().getColor(R.color.white))
                btn3.setBackgroundColor(getResources().getColor(R.color.grey1))

            }

            3 -> {
                btn1.setBackgroundColor(getResources().getColor(R.color.grey1))
                btn2.setBackgroundColor(getResources().getColor(R.color.grey1))
                btn3.setBackgroundColor(getResources().getColor(R.color.white))

            }
        }
    }


    @SuppressLint("UseCompatLoadingForDrawables")
    fun diodeColorChanged(btnNum: Int){
        binding.redButton.background = getDrawable(R.drawable.red_btn_inactive)
        binding.greenButton.background = getDrawable(R.drawable.green_btn_inactive)
        binding.blueButton.background = getDrawable(R.drawable.blue_btn_inactive)
        binding.whiteButton.background = getDrawable(R.drawable.white_btn_inactive)
        when (btnNum){
            1 -> {
                binding.redButton.background = getDrawable(R.drawable.red_btn_active)
            }

            2 -> {
                binding.greenButton.background = getDrawable(R.drawable.green_btn_active)
            }

            3 -> {
                binding.blueButton.background = getDrawable(R.drawable.blue_btn_active)
            }

            4 -> {
                binding.whiteButton.background = getDrawable(R.drawable.white_btn_active)
            }
        }
    }




}