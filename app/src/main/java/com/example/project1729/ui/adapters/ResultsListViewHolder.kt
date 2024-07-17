package com.example.project1729.ui.adapters

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.project1729.R
import com.example.project1729.domain.model.Check

class ResultsListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val date: TextView = itemView.findViewById(R.id.DateAndTimeValue)
    private val eye: TextView = itemView.findViewById(R.id.EyeValue)
    private val measurementMethod: TextView = itemView.findViewById(R.id.MeasurementMethodValue)
    private val diodeColor: TextView = itemView.findViewById(R.id.DiodeColorValue)
    private val KCHSM: TextView = itemView.findViewById(R.id.KCHSMValue)

    fun bind(model: Check){
        date.text = model.dateAndTime
        eye.text = model.eye
        measurementMethod.text = model.measurementMethod
        diodeColor.text = model.diodeColor
        KCHSM.text = "${model.KCHSM} Гц"

    }

}