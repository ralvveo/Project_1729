package com.example.project1729.ui.adapters

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.project1729.R
import com.example.project1729.domain.model.Test

class HistoryContentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val date: TextView = itemView.findViewById(R.id.DateAndTimeValue)
    private val result: TextView = itemView.findViewById(R.id.ResultValue)

    fun bind(model: Test){
        date.text = model.dateAndTime
        result.text = model.result

    }

}