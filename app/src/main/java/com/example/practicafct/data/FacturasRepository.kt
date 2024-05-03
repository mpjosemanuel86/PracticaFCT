package com.example.practicafct.data

import com.example.practicafct.data.retrofit.FacturaService
import com.example.practicafct.data.room.FacturaDatabase
import com.example.practicafct.data.room.FacturaModelRoom

// Repositorio para manejar la obtención y almacenamiento de facturas
class FacturasRepository() {
    // Instancia del servicio Retrofit para obtener datos de facturas desde una API
    val api = FacturaService()

    // Instancia del DAO (Data Access Object) para interactuar con la base de datos Room
    val facturaDAO = FacturaDatabase.getAppDBInstance().getFacturaDao()

    // Función suspendida para obtener las facturas desde la API
    suspend fun getFacturas(): List<FacturaModelRoom>? {
        return api.getFacturas()
    }

    // Función suspendida para insertar facturas en la base de datos Room
    suspend fun insertFacturasInRoom(facturas: List<FacturaModelRoom>) {
        facturaDAO.insertFacturasInRoom(facturas)
    }

    // Función para obtener todas las facturas almacenadas en la base de datos Room
    fun getAllFacturasFromRoom(): List<FacturaModelRoom> {
        return facturaDAO.getAllFacturasFromRoom()
    }

    // Función suspendida para obtener facturas desde la API y luego insertarlas en la base de datos Room
    suspend fun fetchAndInsertFacturasFromAPI() {
        facturaDAO.deleteAllFacturasFromRoom()
        // Obtiene las facturas desde la API
        val facturasFromAPI = getFacturas() ?: emptyList()
        // Mapea las facturas obtenidas desde la API a objetos FacturaModelRoom
        val facturasRoom = facturasFromAPI.map { invoice ->
            FacturaModelRoom(
                descEstado = invoice.descEstado,
                importeOrdenacion = invoice.importeOrdenacion,
                fecha = invoice.fecha
            )
        }
        // Inserta las facturas mapeadas en la base de datos Room
        insertFacturasInRoom(facturasRoom)
    }

    suspend fun getFacturasMock(): List<FacturaModelRoom>?{
        return api.getFacturasRetroMock()
    }

    suspend fun fetchAndInsertFacturasFromMock(){
        facturaDAO.deleteAllFacturasFromRoom()
        val facturasFromMock = getFacturasMock()?: emptyList()
        val facturasRoom = facturasFromMock.map { factura ->
            FacturaModelRoom(
                descEstado = factura.descEstado,
                importeOrdenacion = factura.importeOrdenacion,
                fecha = factura.fecha

            )
        }

        insertFacturasInRoom(facturasRoom)
    }
}
