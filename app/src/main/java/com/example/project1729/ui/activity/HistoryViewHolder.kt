package com.example.project1729.ui.activity

import android.view.View
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources.getDrawable

import androidx.constraintlayout.widget.ConstraintLayout


import androidx.recyclerview.widget.RecyclerView
import com.example.project1729.R
import com.example.project1729.domain.model.ConvertedMeasurement
import com.example.project1729.domain.model.Measurement


class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val date: TextView = itemView.findViewById(R.id.DateAndTimeValue)
    private val device: TextView = itemView.findViewById(R.id.DeviceValue)
    private val eye: TextView = itemView.findViewById(R.id.EyeValue)
    private val measurementMethod: TextView = itemView.findViewById(R.id.MeasurementMethodValue)
    private val diodeColor: TextView = itemView.findViewById(R.id.DiodeColorValue)
    private val KCHSM: TextView = itemView.findViewById(R.id.KCHSMValue)
    private val historyItem: ConstraintLayout = itemView.findViewById(R.id.historyItem)


    val context = this.itemView.context


    fun bind(model: ConvertedMeasurement){
        date.text = model.dateAndTime
        device.text = model.device
        eye.text = model.eye
        measurementMethod.text = model.measurementMethod
        diodeColor.text = model.diodeColor
        KCHSM.text = model.KCHSM
        when (model.backgroundColor){
            "yellow" -> historyItem.background = getDrawable(context, R.drawable.shape_yellow)
            "pinky" -> historyItem.background = getDrawable(context, R.drawable.shape_pinky)
            "blue" -> historyItem.background = getDrawable(context, R.drawable.shape_blue)
            "purple" -> historyItem.background = getDrawable(context, R.drawable.shape_purple)
        }

    }

}