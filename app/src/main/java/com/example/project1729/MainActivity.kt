package com.example.project1729

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ScrollView

class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val testingEyeButton1 = findViewById<Button>(R.id.testingEyeButton1)
        val testingEyeButton2 = findViewById<Button>(R.id.testingEyeButton2)

        val measurementMethodButton1 = findViewById<Button>(R.id.measurementMethodButton1)
        val measurementMethodButton2 = findViewById<Button>(R.id.measurementMethodButton2)
        val measurementMethodButton3 = findViewById<Button>(R.id.measurementMethodButton3)

        val radiationIntensityButton1 = findViewById<Button>(R.id.radiationIntensityButton1)
        val radiationIntensityButton2 = findViewById<Button>(R.id.radiationIntensityButton2)
        val radiationIntensityButton3 = findViewById<Button>(R.id.radiationIntensityButton3)


        testingEyeButton1.setOnClickListener {
            testingEyeButtonPressed(1)
        }

        testingEyeButton2.setOnClickListener {
            testingEyeButtonPressed(2)
        }


        measurementMethodButton1.setOnClickListener {
            measurementMethodButtonPressed(1)
        }

        measurementMethodButton2.setOnClickListener {
            measurementMethodButtonPressed(2)
        }

        measurementMethodButton3.setOnClickListener {
            measurementMethodButtonPressed(3)
        }


        radiationIntensityButton1.setOnClickListener {
            radiationIntensityButtonPressed(1)
        }

        radiationIntensityButton2.setOnClickListener {
            radiationIntensityButtonPressed(2)
        }

        radiationIntensityButton3.setOnClickListener {
            radiationIntensityButtonPressed(3)
        }
    }


    fun testingEyeButtonPressed(btnNum: Int){
        val btn1 = findViewById<Button>(R.id.testingEyeButton1)
        val btn2 = findViewById<Button>(R.id.testingEyeButton2)
        if (btnNum == 1){
            btn1.setBackgroundColor(getResources().getColor(R.color.white))
            btn2.setBackgroundColor(getResources().getColor(R.color.grey1))

        }
        if (btnNum == 2){
            btn1.setBackgroundColor(getResources().getColor(R.color.grey1))
            btn2.setBackgroundColor(getResources().getColor(R.color.white))

        }
    }

    fun measurementMethodButtonPressed(btnNum: Int){
        val btn1 = findViewById<Button>(R.id.measurementMethodButton1)
        val btn2 = findViewById<Button>(R.id.measurementMethodButton2)
        val btn3 = findViewById<Button>(R.id.measurementMethodButton3)

        if (btnNum == 1){
            btn1.setBackgroundColor(getResources().getColor(R.color.white))
            btn2.setBackgroundColor(getResources().getColor(R.color.grey1))
            btn3.setBackgroundColor(getResources().getColor(R.color.grey1))

        }
        if (btnNum == 2){
            btn1.setBackgroundColor(getResources().getColor(R.color.grey1))
            btn2.setBackgroundColor(getResources().getColor(R.color.white))
            btn3.setBackgroundColor(getResources().getColor(R.color.grey1))

        }

        if (btnNum == 3){
            btn1.setBackgroundColor(getResources().getColor(R.color.grey1))
            btn2.setBackgroundColor(getResources().getColor(R.color.grey1))
            btn3.setBackgroundColor(getResources().getColor(R.color.white))

        }
    }

    fun radiationIntensityButtonPressed(btnNum: Int){
        val btn1 = findViewById<Button>(R.id.radiationIntensityButton1)
        val btn2 = findViewById<Button>(R.id.radiationIntensityButton2)
        val btn3 = findViewById<Button>(R.id.radiationIntensityButton3)

        if (btnNum == 1){
            btn1.setBackgroundColor(getResources().getColor(R.color.white))
            btn2.setBackgroundColor(getResources().getColor(R.color.grey1))
            btn3.setBackgroundColor(getResources().getColor(R.color.grey1))

        }
        if (btnNum == 2){
            btn1.setBackgroundColor(getResources().getColor(R.color.grey1))
            btn2.setBackgroundColor(getResources().getColor(R.color.white))
            btn3.setBackgroundColor(getResources().getColor(R.color.grey1))

        }

        if (btnNum == 3){
            btn1.setBackgroundColor(getResources().getColor(R.color.grey1))
            btn2.setBackgroundColor(getResources().getColor(R.color.grey1))
            btn3.setBackgroundColor(getResources().getColor(R.color.white))

        }
    }




}