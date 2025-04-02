package com.example.project1729.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.project1729.R
import com.example.project1729.domain.model.Test

class HistoryContentAdapter(
    private val history: List <Test>
) : RecyclerView.Adapter<HistoryContentViewHolder> (){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryContentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.history_content_item, parent, false)
        return HistoryContentViewHolder(view)
    }



    override fun onBindViewHolder(holder: HistoryContentViewHolder, position: Int) {
        holder.bind(history[position])
    }

    override fun getItemCount(): Int {
        return history.size
    }

}