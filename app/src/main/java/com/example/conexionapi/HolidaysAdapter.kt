package com.example.conexionapi

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class HolidaysAdapter(private var holidays: List<SamirResponse>):

    RecyclerView.Adapter<HolidaysAdapter.ViewHolder>() {

    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        fun bind(holidays: SamirResponse){
            itemView.setOnClickListener{
                println("item pulsado")
            }
            itemView.findViewById<TextView>(R.id.tvTitle).text = holidays.titulo
            itemView.findViewById<TextView>(R.id.tvFechaFestividad).text = holidays.ubicacion
            itemView.findViewById<TextView>(R.id.tvDescripcion).text = holidays.descripcion

        }

    }

    fun setList(lista:MutableList<SamirResponse>){
        this.holidays = lista
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_obj, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(holidays[position])
    }

    override fun getItemCount() = holidays.size


}