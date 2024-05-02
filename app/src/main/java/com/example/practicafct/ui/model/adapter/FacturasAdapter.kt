package com.example.practicafct.ui.model.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.practicafct.R
import com.example.practicafct.data.room.FacturaModelRoom

// Adaptador para mostrar una lista de facturas en un RecyclerView
class FacturasAdapter(
    private var facturasList: List<FacturaModelRoom> // Lista de facturas a mostrar
) : RecyclerView.Adapter<FacturasViewHolder>() {

    // Crea y devuelve una instancia de FacturasViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FacturasViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        // Infla el diseño de un elemento de factura
        return FacturasViewHolder(layoutInflater.inflate(R.layout.item_facturas, parent, false))
    }

    // Devuelve el número total de elementos en la lista de facturas
    override fun getItemCount(): Int = facturasList.size

    // Vincula los datos de una factura en la posición dada con la vista correspondiente en el ViewHolder
    override fun onBindViewHolder(holder: FacturasViewHolder, position: Int) {
        val item: FacturaModelRoom = facturasList[position]
        // Renderiza los datos de la factura en el ViewHolder
        holder.render(item)
    }

    // Función para actualizar los datos del adaptador con una nueva lista de facturas
    fun updateFacturas(newFacturasList: List<FacturaModelRoom>) {
        facturasList = newFacturasList
        notifyDataSetChanged() // Notifica al adaptador que los datos han cambiado y necesita refrescarse
    }
}
