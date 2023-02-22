package com.example.conexionapi

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class HolidaysViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    val title: TextView = itemView.findViewById(R.id.tvTitle)
    val description: TextView = itemView.findViewById(R.id.tvDescripcion)
    val image: TextView = itemView.findViewById(R.id.tvFechaFestividad)

}