package com.example.project1729.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.project1729.R
import com.example.project1729.domain.model.Check

class ResultsListAdapter(
    private val history: List<Check>
) : RecyclerView.Adapter<ResultsListViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultsListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.results_list_item, parent, false)
        return ResultsListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ResultsListViewHolder, position: Int) {
        holder.bind(history[position])
    }

    override fun getItemCount(): Int {
        return history.size
    }

}