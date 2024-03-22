package com.example.project1729.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project1729._unsorted.basic.HistoryAdapter
import com.example.project1729._unsorted.basic.Measurement
import com.example.project1729.databinding.ActivityHistoryBinding

class HistoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHistoryBinding

    val historyList : MutableList<Measurement> = mutableListOf()

    val test1: Measurement = Measurement(dateAndTime = "18.02.2024 13:03",
        device = "98:D3:31:FB:12:A9",
        eye = "Правый",
        measurementMethod = "Прямой",
        diodeColor = "Красный",
        KCHSM = "145",
        backgroundColor = "blue")

    val test2: Measurement = Measurement(dateAndTime = "02.02.2024 9:01",
        device = "98:D3:31:FB:12:A9",
        eye = "Правый",
        measurementMethod = "Обратный",
        diodeColor = "Синий",
        KCHSM = "205",
        backgroundColor = "yellow")

    val test3: Measurement = Measurement(dateAndTime = "27.01.2024 21:04",
        device = "35:FC:5F:BF:BD:CB",
        eye = "Левый",
        measurementMethod = "Прямой",
        diodeColor = "Белый",
        KCHSM = "145",
        backgroundColor = "pinky")

    val test4: Measurement = Measurement(dateAndTime = "27.01.2024 21:04",
        device = "35:FC:5F:BF:BD:CB",
        eye = "Левый",
        measurementMethod = "Прямой",
        diodeColor = "Белый",
        KCHSM = "145",
        backgroundColor = "purple")

    val test5: Measurement = Measurement(dateAndTime = "27.01.2024 21:04",
        device = "35:FC:5F:BF:BD:CB",
        eye = "Левый",
        measurementMethod = "Прямой",
        diodeColor = "Белый",
        KCHSM = "145",
        backgroundColor = "blue")

    val test6: Measurement = Measurement(dateAndTime = "27.01.2024 21:04",
        device = "35:FC:5F:BF:BD:CB",
        eye = "Левый",
        measurementMethod = "Прямой",
        diodeColor = "Белый",
        KCHSM = "145",
        backgroundColor = "yellow")


    val lastPlaceholder: Measurement = Measurement(dateAndTime = "27.01.2024 21:04",
        device = "35:FC:5F:BF:BD:CB",
        eye = "Левый",
        measurementMethod = "Прямой",
        diodeColor = "Белый",
        KCHSM = "145",
        backgroundColor = "null")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        historyList.add(test1)
        historyList.add(test2)
        historyList.add(test3)
        historyList.add(test4)
        historyList.add(test5)
        historyList.add(test6)


        //История измерений
        binding.historyList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.historyList.adapter = HistoryAdapter(historyList)
    }
}