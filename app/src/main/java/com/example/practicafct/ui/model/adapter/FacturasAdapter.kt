package com.example.practicafct.ui.model.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.practicafct.R
import com.example.practicafct.data.room.FacturaModelRoom

class FacturasAdapter(
    private var facturasList: List<FacturaModelRoom>,) : RecyclerView.Adapter<FacturasViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FacturasViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return FacturasViewHolder(layoutInflater.inflate(R.layout.item_facturas, parent, false))
    }

    override fun getItemCount(): Int = facturasList.size

    override fun onBindViewHolder(holder: FacturasViewHolder, position: Int) {
        val item: FacturaModelRoom = facturasList[position]
        holder.render(item)

    }

    // Función para actualizar los datos del adaptador
    fun updateFacturas(newFacturasList: List<FacturaModelRoom>) {
        facturasList = newFacturasList
        notifyDataSetChanged()
    }
}
