package com.example.practicafct.ui.model.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.practicafct.data.retrofit.reponse.Facturas
import com.example.practicafct.data.room.FacturaModelRoom
import com.example.practicafct.databinding.ItemFacturasBinding

class FacturasViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    fun setAlertDialog(){

        val builder = android.app.AlertDialog.Builder(itemView.context)
        builder.setTitle("Información")
        builder.setMessage("Esta funcionalidad aún no está disponible")
        builder.setPositiveButton("Cerrar") { dialog, _ ->
            dialog.dismiss()
        }

        val dialog = builder.create()
        dialog.show()

    }

    val binding = ItemFacturasBinding.bind(view)

    fun render(facturasModel: FacturaModelRoom) {
        binding.tvFechaFactura.text = facturasModel.fecha
        binding.tvEstadoFactura.text = facturasModel.descEstado
        binding.tvImporteFactura.text = facturasModel.importeOrdenacion.toString()
        binding.clRecyclerFacturas.setOnClickListener{
            setAlertDialog()
        }

    }


}