package com.example.practicafct.ui.model.adapter

import android.content.DialogInterface.OnClickListener
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.practicafct.Facturas
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.practicafct.R
import com.example.practicafct.ui.model.adapter.FacturasViewHolder

class FacturasAdapter(
    private var facturasList: List<Facturas>,
    private val onClickListener: (Facturas) -> Unit
) : RecyclerView.Adapter<FacturasViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FacturasViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return FacturasViewHolder(layoutInflater.inflate(R.layout.item_facturas, parent, false))
    }

    override fun onBindViewHolder(holder: FacturasViewHolder, position: Int) {
        val item = facturasList[position]
        holder.render(item, onClickListener)
    }

    override fun getItemCount(): Int = facturasList.size

    // Función para actualizar los datos del adaptador
    fun updateData(newFacturasList: List<Facturas>) {
        facturasList = newFacturasList
        notifyDataSetChanged()
    }
}
