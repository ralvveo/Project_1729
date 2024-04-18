package com.example.project1729.ui.activity

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.project1729.R
import com.example.project1729.domain.model.ConvertedMeasurement
import com.example.project1729.domain.model.Measurement

class HistoryAdapter(
    private val history: List <ConvertedMeasurement>
) : RecyclerView.Adapter<HistoryViewHolder> (){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.history_item, parent, false)
        return HistoryViewHolder(view)
    }



    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(history[position])
    }

    override fun getItemCount(): Int {
        return history.size
    }

}