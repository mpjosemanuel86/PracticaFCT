package com.example.practicafct.adapter

import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.practicafct.Facturas
import com.example.practicafct.databinding.ItemFacturasBinding

class FacturasViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    val binding = ItemFacturasBinding.bind(view)

    fun render(facturasModel: Facturas, onClickListener: (Facturas) -> Unit) {
        binding.tvFechaFactura.text = facturasModel.fecha
        binding.tvEstadoFactura.text = facturasModel.descEstado
        binding.tvImporteFactura.text = facturasModel.importeOrdenacion.toString()
        itemView.setOnClickListener {onClickListener(facturasModel)}
    }
}