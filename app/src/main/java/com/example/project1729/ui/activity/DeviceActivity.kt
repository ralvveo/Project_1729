package com.example.project1729.ui.activity

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project1729._unsorted.bt.basic.BluetoothConstants
import com.example.project1729._unsorted.bt.basic.ItemAdapter
import com.example.project1729._unsorted.bt.basic.ListItem
import com.example.bt.checkBtPermission
import com.example.project1729.R
import com.example.project1729.databinding.ActivityDeviceBinding
import com.google.android.material.snackbar.Snackbar

class DeviceActivity : AppCompatActivity(), ItemAdapter.Listener {
    private var preference: SharedPreferences? = null
    private lateinit var itemAdapter: ItemAdapter
    private lateinit var discoveryAdapter: ItemAdapter
    private var bAdapter: BluetoothAdapter? = null
    private lateinit var binding: ActivityDeviceBinding
    private lateinit var btLauncher: ActivityResultLauncher<Intent>
    private lateinit var pLauncher: ActivityResultLauncher<Array<String>>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeviceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preference = getSharedPreferences(BluetoothConstants.PREFERENCE, Context.MODE_PRIVATE)
        binding.imBtOn.setOnClickListener{
            btLauncher.launch(Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE))
        }
        binding.imBtSearch.setOnClickListener {
            try {bAdapter?.startDiscovery()} catch (e: SecurityException) {}
        }
        intentFilters()
        checkPermissions()
        initRcViews()
        registerBtLauncher()
        initBtAdapter()
        bluetoothState()



    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        preference = activity?.getSharedPreferences(BluetoothConstants.PREFERENCE, Context.MODE_PRIVATE)
//        binding.imBtOn.setOnClickListener{
//            btLauncher.launch(Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE))
//        }
//        binding.imBtSearch.setOnClickListener {
//            try {bAdapter?.startDiscovery()} catch (e: SecurityException) {}
//        }
//        intentFilters()
//        checkPermissions()
//        initRcViews()
//        registerBtLauncher()
//        initBtAdapter()
//        bluetoothState()
//    }

    private fun initRcViews()  {
        binding.rcViewPaired.layoutManager = LinearLayoutManager(this)
        binding.rcViewSearched.layoutManager = LinearLayoutManager(this)
        itemAdapter= ItemAdapter(this, false);
        discoveryAdapter= ItemAdapter(this, true);
        binding.rcViewPaired.adapter = itemAdapter
        binding.rcViewSearched.adapter = discoveryAdapter
    }

    private fun getPairedDevices(){
        try {
            val list = ArrayList<ListItem>()
            val deviceList = bAdapter?.bondedDevices as Set<BluetoothDevice>
            deviceList.forEach{
                list.add(
                    ListItem(
                        it,
                        preference?.getString(BluetoothConstants.MAC, "") == it.address //если мак адрес уже сохранен, то будет тру, если нет - фалс
                    )
                )
            }
            binding.textNoPaired.visibility = if(list.isEmpty()) View.VISIBLE else View.GONE
            itemAdapter.submitList(list)
        } catch (e: SecurityException){

        }

    }

    private fun initBtAdapter(){
        val bManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bAdapter = bManager.adapter
    }

    private fun bluetoothState(){
        if (bAdapter?.isEnabled == true) {
            binding.imBtOn.setBackgroundColor(getResources().getColor(R.color.green))
        //changeButtonColor(binding.imBtOn, Color.GREEN)
        getPairedDevices()}
    }

    private fun registerBtLauncher(){
        btLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ){
            if (it.resultCode == Activity.RESULT_OK){
                binding.imBtOn.setBackgroundColor(getResources().getColor(R.color.green))
                //changeButtonColor(binding.imBtOn, Color.GREEN)
                Snackbar.make(binding.root, "Bluetooth включен.", Snackbar.LENGTH_LONG).show()
                getPairedDevices()
            } else {
                Snackbar.make(binding.root, "Необходимо включить bluetooth!", Snackbar.LENGTH_LONG).show()
            }
        }
    }

    private fun checkPermissions(){
        if(!checkBtPermission()) {
            registerPermissionListener()
            launchPermission()
        }
    }

    private fun launchPermission(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            pLauncher.launch(arrayOf(Manifest.permission.BLUETOOTH_CONNECT, Manifest.permission.ACCESS_FINE_LOCATION))
        } else {
            pLauncher.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION))
        }
    }

    private fun registerPermissionListener() {
        pLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {

        }
    }

    private fun saveMac(mac: String){ //для сохранения мак адреса выбранного устройства
        val editor = preference?.edit()
        editor?.putString(BluetoothConstants.MAC, mac)
        editor?.apply()
    }

    override fun onClick(item: ListItem) { //для выбора сохраняемого устройства
        saveMac(item.device.address)
    }

    private val bReceiver = object : BroadcastReceiver(){
        override fun onReceive(p0: Context?, intent: Intent?) {
           if(intent?.action == BluetoothDevice.ACTION_FOUND) {
               val device = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
               val list = mutableSetOf<ListItem>()
               list.addAll(discoveryAdapter.currentList)
               if (device!= null) list.add(ListItem(device, false))
               discoveryAdapter.submitList(list.toList())
               try {Log.d("Mylog", "Device: ${device?.name}")} catch (e: SecurityException) {}

           } else if(intent?.action == BluetoothDevice.ACTION_BOND_STATE_CHANGED){
               getPairedDevices()
           } else if(intent?.action == BluetoothAdapter.ACTION_DISCOVERY_FINISHED) {

           }
        }

    }

    private fun intentFilters(){
        val f1 = IntentFilter(BluetoothDevice.ACTION_FOUND)
        val f2 = IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED)
        val f3 = IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
        registerReceiver(bReceiver, f1)
        registerReceiver(bReceiver, f2)
        registerReceiver(bReceiver, f3)
    }

}