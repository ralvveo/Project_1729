package com.example.project1729.ui.activity

import android.R as androidR
import com.example.project1729.R
import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project1729.databinding.ActivityHistoryBinding
import com.example.project1729.domain.converter.MeasurementDispayConverter
import com.example.project1729.domain.model.ConvertedMeasurement
import com.example.project1729.domain.model.Measurement
import com.example.project1729.ui.adapters.HistoryAdapter
import com.example.project1729.ui.view_model.HistoryViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class HistoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHistoryBinding
    private val viewModel by viewModel <HistoryViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)


        viewModel.getHistoryLiveData().observe(this){historyList ->
            render(historyList)
        }
        //Кнопка Очистить историю
        binding.clearHistoryButton.setOnClickListener {
            showClearHistoryAlert()
        }
        //История измерений
        binding.historyList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true)
    }

    private fun render(historyList: MutableList<Measurement>){
        val convertedHistoryList = mutableListOf<ConvertedMeasurement>()
        for (i in historyList){
            convertedHistoryList.add(MeasurementDispayConverter.convert(i))
        }
        binding.historyList.adapter = HistoryAdapter(convertedHistoryList)
        if (historyList.isNotEmpty())
            binding.clearHistoryButton.visibility = View.VISIBLE
        else
            binding.clearHistoryButton.visibility = View.GONE

    }

    private fun showClearHistoryAlert() {
        AlertDialog.Builder(this) //set icon
            .setIcon(androidR.drawable.ic_dialog_alert) //set title
            .setTitle(R.string.dialog_alert) //set message
            .setMessage(R.string.dialog_alert_cannot_be_undone) //set positive button
            .setPositiveButton(
                "Да",
                DialogInterface.OnClickListener { dialogInterface, i -> //set what would happen when positive button is clicked
                    viewModel.clearHistory()
                    Toast.makeText(getApplicationContext(),R.string.history_deleted ,Toast.LENGTH_LONG).show();
                }) //set negative button
            .setNegativeButton(
                "Нет",
                DialogInterface.OnClickListener { dialogInterface, i -> //set what should happen when negative button is clicked
                })
            .show()
    }
}