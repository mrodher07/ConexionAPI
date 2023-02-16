package com.example.conexionapi

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class HolidaysAdapter(private var holidays: List<Holidays>):
    RecyclerView.Adapter<HolidaysAdapter.ViewHolder>() {

    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        fun bind(holidays: Holidays){
            itemView.setOnClickListener{
                println("item pulsado")
            }
            itemView.findViewById<TextView>(R.id.tvTitle).text = holidays.name
            itemView.findViewById<TextView>(R.id.tvFechaFestividad).text = holidays.date.toString()
            itemView.findViewById<TextView>(R.id.tvLocalizacion).text = holidays.locations
            itemView.findViewById<TextView>(R.id.tvTipoFestividad).text = holidays.primaryType
            itemView.findViewById<TextView>(R.id.tvDescripcion).text = holidays.description
        }
    }

    fun setList(lista:MutableList<Holidays>){
        this.holidays = lista
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount() = holidays.size

}