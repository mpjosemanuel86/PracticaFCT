package com.example.practicafct.data


import com.example.practicafct.data.retrofit.FacturaService
import com.example.practicafct.data.room.FacturaDatabase
import com.example.practicafct.data.room.FacturaModelRoom


class FacturasRepository() {
    val api = FacturaService()
    val facturaDAO = FacturaDatabase.getAppDBInstance().getFacturaDao()



    suspend fun getFacturas(): List<FacturaModelRoom>?{
        return api.getFacturas()
    }

    suspend fun insertFacturasInRoom(facturas: List<FacturaModelRoom>) {
        facturaDAO.insertFacturasInRoom(facturas)
    }

    fun getAllFacturasFromRoom(): List<FacturaModelRoom> {
        return facturaDAO.getAllFacturasFromRoom()
    }


    suspend fun fetchAndInsertFacturasFromAPI() {
        val facturasFromAPI = getFacturas() ?: emptyList()
        val facturasRoom = facturasFromAPI.map { invoice ->
            FacturaModelRoom(
                descEstado = invoice.descEstado,
                importeOrdenacion = invoice.importeOrdenacion,
                fecha = invoice.fecha
            )
        }
        insertFacturasInRoom(facturasRoom)

    }
}

