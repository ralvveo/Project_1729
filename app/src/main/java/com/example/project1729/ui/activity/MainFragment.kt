//package com.example.project1729.ui.activity
//
//import android.content.Intent
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.Button
//import android.widget.SeekBar
//import android.widget.SeekBar.OnSeekBarChangeListener
//import androidx.fragment.app.Fragment
//import androidx.lifecycle.ViewModelProvider
//import com.example.project1729.App.Companion.bluetoothConnected
//import com.example.project1729.App.Companion.bluetoothController
//import com.example.project1729.R
//import com.example.project1729.bt.bluetooth.BluetoothController
//import com.example.project1729.databinding.FragmentMainBinding
//
//
//
//class MainFragment : Fragment(), BluetoothController.Listener {
//
//    private lateinit var binding: FragmentMainBinding
//    private lateinit var viewModel: MainViewModel
//
//    private var flag = 1
//    private var mes1 = "1"
//    private var mes2 = "1"
//    private var mes3 = "1"
//
//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//
//        binding = FragmentMainBinding.inflate(inflater, container, false)
//        return binding.root
//    }
//
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        viewModel = ViewModelProvider(this, MainViewModel.factory())[MainViewModel::class.java]
//
//        viewModel.getState().observe(viewLifecycleOwner){state ->
//            render(state)
//        }
//
//        //Кнопка списка устройств
//        binding.availableDevicesButton.setOnClickListener {
//            val displayIntent = Intent(requireActivity(), DeviceActivity::class.java)
//            startActivity(displayIntent)
//        }
//
//        //Кнопка подключиться
//        binding.stopButton.setOnClickListener {
//            viewModel.connect()
//        }
//
//        //Кнопки выбора тестируемого глаза
//        binding.testingEyeButton1.setOnClickListener {
//            viewModel.testingEyeButtonPressed(1)
//        }
//
//        binding.testingEyeButton2.setOnClickListener {
//            viewModel.testingEyeButtonPressed(2)
//        }
//
//        //Кнопки выбора метода измерения
//
//        binding.measurementMethodButton1.setOnClickListener {
//            viewModel.measurementMethodButtonPressed(2)
//        }
//
//        binding.measurementMethodButton2.setOnClickListener {
//            viewModel.measurementMethodButtonPressed(3)
//        }
//
//        binding.measurementMethodButton3.setOnClickListener {
//            viewModel.measurementMethodButtonPressed(4)
//        }
//
//        //Кнопки выбора интенсивности
//
//        binding.radiationIntensityButtonAuto.setOnClickListener {
//            viewModel.radiationIntensityButtonPressed(1)
//        }
//
//        binding.radiationIntensityButton1.setOnClickListener {
//            viewModel.radiationIntensityButtonPressed(2)
//        }
//
//        binding.radiationIntensityButton2.setOnClickListener {
//            viewModel.radiationIntensityButtonPressed(3)
//        }
//
//        binding.radiationIntensityButton3.setOnClickListener {
//            viewModel.radiationIntensityButtonPressed(4)
//        }
//
//        //Кнопки выбора цвета
//        binding.redButton.setOnClickListener {
//            viewModel.diodeColorChanged(1)
//        }
//
//        binding.greenButton.setOnClickListener {
//            viewModel.diodeColorChanged(2)
//        }
//
//        binding.blueButton.setOnClickListener {
//            viewModel.diodeColorChanged(3)
//        }
//
//        binding.whiteButton.setOnClickListener {
//            viewModel.diodeColorChanged(4)
//        }
//
//        //Кнопка измерения КЧСМ
//        binding.measureKCHSMButton.setOnClickListener {
////            val displayIntent = Intent(this, MeasureActivity::class.java)
////            startActivity(displayIntent)
//            viewModel.startMeasure()
//        }
//
//
//
//        //Кнопка настройки
//        binding.settingsButton.setOnClickListener {
//            parentFragmentManager.beginTransaction()
//                .replace(R.id.rootFragmentContainerView, SettingsFragment())
//                .addToBackStack("SettingsFragment")
//                .commit()
//        }
//
//        //Кнопка история
//        binding.historyButton.setOnClickListener {
//            val displayIntent = Intent(requireActivity(), HistoryActivity::class.java)
//            startActivity(displayIntent)
//        }
//
//        //Позунок регулировки интесивности
//        binding.radiationIntentsitySlider.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
//            var currentProgress = 0
//            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
//                currentProgress = progress
//            }
//            override fun onStartTrackingTouch(seekBar: SeekBar) {
//            }
//            override fun onStopTrackingTouch(seekBar: SeekBar) {
//                viewModel.setSeekBarProgress(currentProgress)
//                val mm = seekBar.progress
//                val str = mm.toString()
//                val strr = "q" + str
//                bluetoothController.sendMessage(strr)
//            }
//        })
//
//
//    }
//
//    private fun render(state: MutableMap<String,String>){
//        binding.redButton.background = requireActivity().getDrawable(R.drawable.red_btn_inactive)
//        binding.greenButton.background = requireActivity().getDrawable(R.drawable.green_btn_inactive)
//        binding.blueButton.background = requireActivity().getDrawable(R.drawable.blue_btn_inactive)
//        binding.whiteButton.background = requireActivity().getDrawable(R.drawable.white_btn_inactive)
//        when (state[BLUETOOTH]){
//             CONNECTED -> {
//                 binding.connectionStatusText.text = "Статус соединения: Подключено"
//                 binding.stopButton.text = "Отключиться"
//                 binding.connectionStatusIcon.background = requireActivity().getDrawable(R.drawable.green_tick)
//             }
//             NOTCONNECTED -> {
//                 binding.connectionStatusText.text = "Статус соединения: Не подключено"
//                 binding.stopButton.text = "Подключиться"
//                 binding.connectionStatusIcon.background = requireActivity().getDrawable(R.drawable.red_cross)
//           }
//        }
//
//        when (state[EYE]){
//            LEFT -> {
//                binding.testingEyeButton1.setBackgroundColor(getResources().getColor(R.color.whiteBlack))
//                binding.testingEyeButton2.setBackgroundColor(getResources().getColor(R.color.grey1White80))
//
//                binding.testingEyeButton1.setTextColor((getResources().getColor(R.color.blackWhite)))
//                binding.testingEyeButton2.setTextColor((getResources().getColor(R.color.black)))
//            }
//            RIGHT -> {
//                binding.testingEyeButton1.setBackgroundColor(getResources().getColor(R.color.grey1White80))
//                binding.testingEyeButton2.setBackgroundColor(getResources().getColor(R.color.whiteBlack))
//
//                binding.testingEyeButton1.setTextColor((getResources().getColor(R.color.black)))
//                binding.testingEyeButton2.setTextColor((getResources().getColor(R.color.blackWhite)))
//            }
//        }
//
//        when (state[METHOD]){
//
//            STRAIGHT -> {
//                buttonChange(btnActive = binding.measurementMethodButton1, btnPassive1 = binding.measurementMethodButton2, btnPassive2 = binding.measurementMethodButton3)
//            }
//            REVERSED -> {
//                buttonChange(btnActive = binding.measurementMethodButton2, btnPassive1 = binding.measurementMethodButton1, btnPassive2 = binding.measurementMethodButton3)
//            }
//            OTHER -> {
//                buttonChange(btnActive = binding.measurementMethodButton3, btnPassive1 = binding.measurementMethodButton1, btnPassive2 = binding.measurementMethodButton2)
//            }
//        }
//
//        when (state[INTENSITY]){
//            AUTO -> {
//                buttonChangeFour(btnActive = binding.radiationIntensityButtonAuto, btnPassive1 = binding.radiationIntensityButton1, btnPassive2 = binding.radiationIntensityButton2, btnPassive3 = binding.radiationIntensityButton3)
//                binding.radiationIntentsitySlider.progress = 50
//            }
//
//            LOW -> {
//                buttonChangeFour(btnActive = binding.radiationIntensityButton1, btnPassive1 = binding.radiationIntensityButtonAuto, btnPassive2 = binding.radiationIntensityButton2, btnPassive3 = binding.radiationIntensityButton3)
//                binding.radiationIntentsitySlider.progress = 20
//            }
//            MIDDLE -> {
//                buttonChangeFour(btnActive = binding.radiationIntensityButton2, btnPassive1 = binding.radiationIntensityButton1, btnPassive2 = binding.radiationIntensityButtonAuto, btnPassive3 = binding.radiationIntensityButton3)
//                binding.radiationIntentsitySlider.progress = 50
//            }
//            HIGH -> {
//                buttonChangeFour(btnActive = binding.radiationIntensityButton3, btnPassive1 = binding.radiationIntensityButton1, btnPassive2 = binding.radiationIntensityButtonAuto, btnPassive3 = binding.radiationIntensityButton2)
//                binding.radiationIntentsitySlider.progress = 80
//            }
//        }
//
//        when (state[DIODECOLOR]){
//            RED -> binding.redButton.background = requireActivity().getDrawable(R.drawable.red_btn_active)
//            GREEN -> binding.greenButton.background = requireActivity().getDrawable(R.drawable.green_btn_active)
//            BLUE -> binding.blueButton.background = requireActivity().getDrawable(R.drawable.blue_btn_active)
//            WHITE -> binding.whiteButton.background = requireActivity().getDrawable(R.drawable.white_btn_active)
//        }
//        binding.temperatureValue.text = state[TEMPERATURE]
//        binding.pressureValue.text = state[PRESSURE]
//        binding.KCHSMValue.text = state[KCHSM]
//
////        when (state[TEMPERATURE]){
////            "" -> {}
////            else -> {
////                binding.temperatureValue.text = state[TEMPERATURE]
////            }
////        }
////
////        when (state[PRESSURE]){
////            "" -> {}
////            else -> {
////                binding.pressureValue.text = state[PRESSURE]
////            }
////        }
//
//
//
//
//
//    }
//
//    private fun buttonChange(btnActive: Button, btnPassive1: Button, btnPassive2: Button){
//        btnActive.setBackgroundColor(getResources().getColor(R.color.whiteBlack))
//        btnPassive1.setBackgroundColor(getResources().getColor(R.color.grey1White80))
//        btnPassive2.setBackgroundColor(getResources().getColor(R.color.grey1White80))
//        btnActive.setTextColor((getResources().getColor(R.color.blackWhite)))
//        btnPassive1.setTextColor((getResources().getColor(R.color.black)))
//        btnPassive2.setTextColor((getResources().getColor(R.color.black)))
//    }
//
//    private fun buttonChangeFour(btnActive: Button, btnPassive1: Button, btnPassive2: Button, btnPassive3: Button){
//        btnActive.setBackgroundColor(getResources().getColor(R.color.whiteBlack))
//        btnPassive1.setBackgroundColor(getResources().getColor(R.color.grey1White80))
//        btnPassive2.setBackgroundColor(getResources().getColor(R.color.grey1White80))
//        btnPassive3.setBackgroundColor(getResources().getColor(R.color.grey1White80))
//        btnActive.setTextColor((getResources().getColor(R.color.blackWhite)))
//        btnPassive1.setTextColor((getResources().getColor(R.color.black)))
//        btnPassive2.setTextColor((getResources().getColor(R.color.black)))
//        btnPassive3.setTextColor((getResources().getColor(R.color.black)))
//    }
//
//
//    override fun onReceive(message: String) {
//        requireActivity().runOnUiThread {
//            when(message) {
//                "bluetooth connected" -> {
//                    bluetoothConnected = true
//                    viewModel.changeConnectionState(bluetoothConnected)
//                    //binding.stopButton.backgroundTintList = AppCompatResources.getColorStateList(this, R.color.red)
//                    //binding.stopButton.text = "Disconnect"
//                }
//                "bluetooth no connected" -> {
//                    //binding.stopButton.backgroundTintList = AppCompatResources.getColorStateList(this, R.color.green)
//                    //binding.stopButton.text = "Connect"
//
//                }
//                else -> {
//                    when (flag) {
//                        1 -> {
//                            mes1 = message
//
//                            flag = 2
//                        }
//                        2 -> {
//                            mes2 = message
//                            mes3 = mes1 + mes2
//                            //binding.tvStatus9.text = mes3
//                            flag = 3
//                        }
//                        3 -> {
//                            mes1 = message
//                            //binding.tvStatus8.text = message
//                            flag = 4
//                        }
//                        4 -> {
//                            mes2 = message
//                            mes3 = mes1 + mes2
//                            //binding.tvStatus8.text = mes3
//                            flag = 5
//                        }
//                        else ->
//                        {}
//                        //{binding.tvStatus2.text = message}
//                    }
//                }
//            }
//        }
//    }
//
//    companion object{
//        const val BLUETOOTH = "bluetooth"
//        const val EYE = "eye"
//        const val METHOD = "method"
//        const val INTENSITYSLIDER = "intensitySlider"
//        const val INTENSITY = "intensity"
//        const val DIODECOLOR = "diodeColor"
//        const val KCHSM = "KCHSM"
//        const val PRESSURE = "pressure"
//        const val TEMPERATURE = "temperature"
//
//        const val CONNECTED = "connected"
//        const val NOTCONNECTED = "notConnected"
//
//        const val LEFT = "left"
//        const val RIGHT = "right"
//
//        const val STRAIGHT = "straight"
//        const val REVERSED = "reversed"
//        const val OTHER = "other"
//
//        const val LOW = "low"
//        const val MIDDLE = "middle"
//        const val HIGH = "high"
//
//        const val RED = "red"
//        const val GREEN = "green"
//        const val BLUE = "blue"
//        const val WHITE = "white"
//    }
//
//}