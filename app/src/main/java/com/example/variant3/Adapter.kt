package com.example.variant3

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class Adapter (private val historyList: List<Task>, private val itemClickListener: (Int) -> Unit) : RecyclerView.Adapter<Adapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val date: TextView = itemView.findViewById(R.id.tv_date)
        val task: TextView = itemView.findViewById(R.id.tv_name)
        val yet: TextView = itemView.findViewById(R.id.tv_detail)
        init {
            itemView.setOnClickListener {
                itemClickListener(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycle, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val task = historyList[position]
        holder.date.text = task.date
        holder.task.text = task.task
        holder.yet.text = task.yet

    }
    override fun getItemCount(): Int = historyList.size
}
