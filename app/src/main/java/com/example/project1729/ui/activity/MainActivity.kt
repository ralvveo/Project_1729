package com.example.project1729.ui.activity

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.project1729._unsorted.bt.basic.BluetoothConstants
import com.example.project1729._unsorted.bt.bluetooth.BluetoothController
import com.example.project1729.R
import com.example.project1729._unsorted.basic.App.Companion.bluetoothConnected
import com.example.project1729.databinding.ActivityMainBinding
import com.example.project1729.ui.view_model.MainViewModel


class MainActivity : AppCompatActivity(), BluetoothController.Listener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var bluetoothController: BluetoothController
    private lateinit var btAdapter: BluetoothAdapter
    private lateinit var viewModel: MainViewModel

    private var flag = 1

    private var mes1 = "1"
    private var mes2 = "1"
    private var mes3 = "1"
    private fun initBtAdapter(){
        val bManager = this.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        btAdapter = bManager.adapter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initBtAdapter()
        viewModel = ViewModelProvider(this, MainViewModel.factory())[MainViewModel::class.java]
        val preference = getSharedPreferences(BluetoothConstants.PREFERENCE, Context.MODE_PRIVATE)
        val mac = preference?.getString(BluetoothConstants.MAC, "")
        bluetoothController = BluetoothController

        //Кнопка списка устройств
        binding.availableDevicesButton.setOnClickListener {
            val displayIntent = Intent(this, DeviceActivity::class.java)
            startActivity(displayIntent)
        }

        //Кнопка подключиться
        binding.stopButton.setOnClickListener {
            if (!bluetoothConnected) {
                bluetoothController.connect(mac ?: "", this)
            }
            else {
                bluetoothController.closeConnection()
                bluetoothConnected = false
            }
            changeConnectionState()
        }

        //Кнопки выбора тестируемого глаза
        binding.testingEyeButton1.setOnClickListener {
            testingEyeButtonPressed(1)
            bluetoothController.sendMessage("l")
        }

        binding.testingEyeButton2.setOnClickListener {
            testingEyeButtonPressed(2)
            bluetoothController.sendMessage("p")
        }

        //Кнопки выбора метода измерения
        binding.measurementMethodButton1.setOnClickListener {
            measurementMethodButtonPressed(1)
        }

        binding.measurementMethodButton2.setOnClickListener {
            measurementMethodButtonPressed(2)
        }

        binding.measurementMethodButton3.setOnClickListener {
            measurementMethodButtonPressed(3)
        }

        //Кнопки выбора интенсивности
        binding.radiationIntensityButton1.setOnClickListener {
            radiationIntensityButtonPressed(1)
            bluetoothController.sendMessage("q20")
        }

        binding.radiationIntensityButton2.setOnClickListener {
            radiationIntensityButtonPressed(2)
            bluetoothController.sendMessage("q50")
        }

        binding.radiationIntensityButton3.setOnClickListener {
            radiationIntensityButtonPressed(3)
            bluetoothController.sendMessage("q80")
        }

        //Кнопки выбора цвета
        binding.redButton.setOnClickListener {
            diodeColorChanged(1)
            bluetoothController.sendMessage("r")
        }

        binding.greenButton.setOnClickListener {
            diodeColorChanged(2)
            bluetoothController.sendMessage("g")
        }

        binding.blueButton.setOnClickListener {
            diodeColorChanged(3)
            bluetoothController.sendMessage("b")
        }

        binding.whiteButton.setOnClickListener {
            diodeColorChanged(4)
            bluetoothController.sendMessage("w")
        }

        //Кнопка измерения КЧСМ
        binding.measureKCHSMButton.setOnClickListener {
//            val displayIntent = Intent(this, MeasureActivity::class.java)
//            startActivity(displayIntent)
            bluetoothController.sendMessage("a")
        }



        //Кнопка настройки
        binding.settingsButton.setOnClickListener {
            val displayIntent = Intent(this, SettingsActivity::class.java)
            startActivity(displayIntent)
        }

        //Кнопка история
        binding.historyButton.setOnClickListener {
            val displayIntent = Intent(this, HistoryActivity::class.java)
            startActivity(displayIntent)
        }

        //Позунок регулировки интесивности
        binding.radiationIntentsitySlider.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            var currentProgress = 0
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                currentProgress = progress
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                when (currentProgress){
                    in 0..33 -> radiationIntensityButtonChange(1)
                    in 34..66 -> radiationIntensityButtonChange(2)
                    in 67..100 -> radiationIntensityButtonChange(3)
                }

                val mm = seekBar.progress
                val str = mm.toString()
                val strr = "q" + str
                bluetoothController.sendMessage(strr)
            }
        })

    }

    private fun changeConnectionState(){
        when(bluetoothConnected){
            true -> {
                binding.connectionStatusText.text = "Статус соединения: Подключено"
                binding.stopButton.text = "Отключиться"
                binding.connectionStatusIcon.background = getDrawable(R.drawable.green_tick)

            }
            false ->{
                binding.connectionStatusText.text = "Статус соединения: Не подключено"
                binding.stopButton.text = "Подключиться"
                binding.connectionStatusIcon.background = getDrawable(R.drawable.red_cross)
            }

        }
    }

    private fun testingEyeButtonPressed(btnNum: Int){
        val btn1 = binding.testingEyeButton1
        val btn2 = binding.testingEyeButton2
        when (btnNum) {
            1 -> {
                btn1.setBackgroundColor(getResources().getColor(R.color.whiteBlack))
                btn2.setBackgroundColor(getResources().getColor(R.color.grey1White80))

                btn1.setTextColor((getResources().getColor(R.color.blackWhite)))
                btn2.setTextColor((getResources().getColor(R.color.black)))

            }
            2 -> {
                btn1.setBackgroundColor(getResources().getColor(R.color.grey1White80))
                btn2.setBackgroundColor(getResources().getColor(R.color.whiteBlack))

                btn1.setTextColor((getResources().getColor(R.color.black)))
                btn2.setTextColor((getResources().getColor(R.color.blackWhite)))

            }
        }
    }

    private fun measurementMethodButtonPressed(btnNum: Int){
        val btn1 = binding.measurementMethodButton1
        val btn2 = binding.measurementMethodButton2
        val btn3 = binding.measurementMethodButton3

        when (btnNum) {
            1 -> {
                buttonChange(btnActive = btn1, btnPassive1 = btn2, btnPassive2 = btn3)

            }
            2 -> {
                buttonChange(btnActive = btn2, btnPassive1 = btn1, btnPassive2 = btn3)
            }

            3 -> {
                buttonChange(btnActive = btn3, btnPassive1 = btn1, btnPassive2 = btn2)

            }
        }
    }

    private fun radiationIntensityButtonPressed(btnNum: Int){
        val btn1 = binding.radiationIntensityButton1
        val btn2 = binding.radiationIntensityButton2
        val btn3 = binding.radiationIntensityButton3
        val seekBar = binding.radiationIntentsitySlider

        when (btnNum) {
            1 -> {
                buttonChange(btnActive = btn1, btnPassive1 = btn2, btnPassive2 = btn3)
                seekBar.progress = 20
            }
            2 -> {
                buttonChange(btnActive = btn2, btnPassive1 = btn1, btnPassive2 = btn3)
                seekBar.progress = 50
            }

            3 -> {
                buttonChange(btnActive = btn3, btnPassive1 = btn1, btnPassive2 = btn2)
                seekBar.progress = 80
            }
        }
    }

    //Подстройка кнопок под ползунок
    fun radiationIntensityButtonChange(btnNum: Int){
        val btn1 = binding.radiationIntensityButton1
        val btn2 = binding.radiationIntensityButton2
        val btn3 = binding.radiationIntensityButton3


        when (btnNum) {
            1 -> {
                buttonChange(btnActive = btn1, btnPassive1 = btn2, btnPassive2 = btn3)
            }
            2 -> {
                buttonChange(btnActive = btn2, btnPassive1 = btn1, btnPassive2 = btn3)
            }

            3 -> {
                buttonChange(btnActive = btn3, btnPassive1 = btn1, btnPassive2 = btn2)
            }
        }


    }
    private fun buttonChange(btnActive: Button, btnPassive1: Button, btnPassive2: Button){
        btnActive.setBackgroundColor(getResources().getColor(R.color.whiteBlack))
        btnPassive1.setBackgroundColor(getResources().getColor(R.color.grey1White80))
        btnPassive2.setBackgroundColor(getResources().getColor(R.color.grey1White80))
        btnActive.setTextColor((getResources().getColor(R.color.blackWhite)))
        btnPassive1.setTextColor((getResources().getColor(R.color.black)))
        btnPassive2.setTextColor((getResources().getColor(R.color.black)))

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

    override fun onReceive(message: String) {
        runOnUiThread {
            when(message) {
                "bluetooth connected" -> {
                    bluetoothConnected = true
                    changeConnectionState()
                    //binding.stopButton.backgroundTintList = AppCompatResources.getColorStateList(this, R.color.red)
                    //binding.stopButton.text = "Disconnect"

                }
                "bluetooth no connected" -> {
                    //binding.stopButton.backgroundTintList = AppCompatResources.getColorStateList(this, R.color.green)
                    //binding.stopButton.text = "Connect"

                }
                else -> {
                    when (flag) {
                        1 -> {
                            mes1 = message

                            flag = 2
                        }
                        2 -> {
                            mes2 = message
                            mes3 = mes1 + mes2
                            //binding.tvStatus9.text = mes3
                            flag = 3
                        }
                        3 -> {
                            mes1 = message
                            //binding.tvStatus8.text = message
                            flag = 4
                        }
                        4 -> {
                            mes2 = message
                            mes3 = mes1 + mes2
                            //binding.tvStatus8.text = mes3
                            flag = 5
                        }
                        else ->
                        {}
                        //{binding.tvStatus2.text = message}
                    }
                }
            }
        }
    }


}