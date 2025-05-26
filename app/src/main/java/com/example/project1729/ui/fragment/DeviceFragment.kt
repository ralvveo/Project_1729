package com.example.project1729.ui.fragment

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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bt.checkBtPermission
import com.example.project1729.R
import com.example.project1729.bt.basic.BluetoothConstants
import com.example.project1729.bt.basic.ItemAdapter
import com.example.project1729.bt.basic.ListItem
import com.example.project1729.databinding.ActivityDeviceBinding
import com.example.project1729.databinding.FragmentDeviceBinding
import com.google.android.material.snackbar.Snackbar
import kotlin.collections.forEach


class DeviceFragment : Fragment(), ItemAdapter.Listener {

    private lateinit var binding: FragmentDeviceBinding
    private var preference: SharedPreferences? = null
    private lateinit var itemAdapter: ItemAdapter
    private lateinit var discoveryAdapter: ItemAdapter
    private var bAdapter: BluetoothAdapter? = null
    private lateinit var btLauncher: ActivityResultLauncher<Intent>
    private lateinit var pLauncher: ActivityResultLauncher<Array<String>>


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentDeviceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        preference = requireActivity().getSharedPreferences(BluetoothConstants.PREFERENCE, Context.MODE_PRIVATE)
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
    private fun initRcViews()  {
        binding.rcViewPaired.layoutManager = LinearLayoutManager(requireActivity())
        binding.rcViewSearched.layoutManager = LinearLayoutManager(requireActivity())
        itemAdapter= ItemAdapter(requireActivity() as ItemAdapter.Listener, false);
        discoveryAdapter= ItemAdapter(requireActivity() as ItemAdapter.Listener, true);
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
            binding.secondPlaceholder.visibility = if(list.isEmpty()) View.VISIBLE else View.GONE
            binding.secondPlaceholderText.visibility = if(list.isEmpty()) View.VISIBLE else View.GONE
            itemAdapter.submitList(list)
        } catch (e: SecurityException){

        }

    }

    private fun initBtAdapter(){
        val bManager = requireActivity().getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bAdapter = bManager.adapter
    }

    private fun bluetoothState(){
        if (bAdapter?.isEnabled == true) {
            //binding.imBtOn.background = getDrawable(R.drawable.bluetooth_icon_active)
            //changeButtonColor(binding.imBtOn, Color.GREEN)
            getPairedDevices()}
    }

    private fun registerBtLauncher(){
        btLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ){
            if (it.resultCode == Activity.RESULT_OK){
                //binding.imBtOn.background = getDrawable(R.drawable.bluetooth_icon_active)
                //changeButtonColor(binding.imBtOn, Color.GREEN)
                Snackbar.make(binding.root, "Bluetooth включен.", Snackbar.LENGTH_LONG).show()
                getPairedDevices()
            } else {
                //binding.imBtOn.background = getDrawable(R.drawable.bluetooth_icon)
                Snackbar.make(binding.root, "Необходимо включить bluetooth!", Snackbar.LENGTH_LONG).show()
            }
        }
    }

    private fun checkPermissions(){
        if(!requireActivity().checkBtPermission()) {
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
        requireActivity().registerReceiver(bReceiver, f1)
        requireActivity().registerReceiver(bReceiver, f2)
        requireActivity().registerReceiver(bReceiver, f3)
    }



}