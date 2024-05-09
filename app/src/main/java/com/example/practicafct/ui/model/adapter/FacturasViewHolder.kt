package com.example.practicafct.ui.model.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.practicafct.data.retrofit.reponse.Facturas
import com.example.practicafct.data.room.FacturaModelRoom
import com.example.practicafct.databinding.ItemFacturasBinding

// ViewHolder para cada elemento de la lista de facturas en el RecyclerView
class FacturasViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    // ViewBinding para acceder a las vistas dentro del layout de cada elemento de factura
    val binding = ItemFacturasBinding.bind(view)

    // Función para configurar y mostrar un cuadro de diálogo de alerta
    fun setAlertDialog() {
        val builder = android.app.AlertDialog.Builder(itemView.context)
        builder.setTitle("Información")
        builder.setMessage("Esta funcionalidad aún no está disponible")
        builder.setPositiveButton("Cerrar") { dialog, _ ->
            dialog.dismiss() // Cierra el cuadro de diálogo
        }
        val dialog = builder.create() // Crea el cuadro de diálogo
        dialog.show() // Muestra el cuadro de diálogo
    }

    // Función para renderizar los datos de una factura en las vistas correspondientes
    fun render(facturasModel: FacturaModelRoom) {
        // Asigna los datos de la factura a las vistas dentro del layout del elemento de factura
        binding.tvFechaFactura.text = facturasModel.fecha
        binding.tvEstadoFactura.text = facturasModel.descEstado
        val importeConEuro = "${facturasModel.importeOrdenacion} €"
        binding.tvImporteFactura.text = importeConEuro

        // Configura un OnClickListener en el elemento de factura para mostrar el cuadro de diálogo de alerta
        binding.clRecyclerFacturas.setOnClickListener {
            setAlertDialog()
        }
    }
}
