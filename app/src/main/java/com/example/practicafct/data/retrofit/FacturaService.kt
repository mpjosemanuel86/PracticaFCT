package com.example.practicafct.data.retrofit

import com.example.practicafct.core.retrofit.RetrofitHelper
import com.example.practicafct.core.retromock.RetroMockHelper
import com.example.practicafct.data.room.FacturaModelRoom

// Esta clase se encarga de manejar las llamadas a la API utilizando Retrofit
class FacturaService {

    // Se obtiene una instancia de Retrofit utilizando un helper
    private val retrofitBuilder = RetrofitHelper.getRetrofit()

    private val retromockBuilder = RetroMockHelper.getRetromock(retrofitBuilder)

    // Función suspendida que obtiene las facturas desde la API de forma asíncrona
    suspend fun getFacturas(): List<FacturaModelRoom>? {
        // Se hace la llamada a la API para obtener las facturas
        val response = retrofitBuilder.create(APIService::class.java).getFacturas()
        // Se extraen las facturas del cuerpo de la respuesta
        val facturas = response.body()?.facturas
        // Se devuelve la lista de facturas
        return facturas
    }

    suspend fun getFacturasRetroMock(): List<FacturaModelRoom>?{
        val reponse =  retromockBuilder.create(RetroMockService::class.java ).getFacturasMock()
        val facturasMock = reponse.body()?.facturas
        return facturasMock

    }
}
