package com.example.practicafct.data.retrofit

import android.util.Log
import com.example.practicafct.data.retrofit.network.Detalles
import com.example.practicafct.core.retrofit.RetrofitHelper
import com.example.practicafct.core.retromock.RetroMockHelper
import com.example.practicafct.data.room.FacturaModelRoom


class FacturaService {

    // Se obtiene una instancia de Retrofit utilizando un helper
    private val retrofitBuilder = RetrofitHelper.getRetrofit()

    private val retromockBuilder = RetroMockHelper.getRetromock(retrofitBuilder)

    // Función suspendida que obtiene las facturas desde la API de forma asíncrona
    suspend fun getFacturas(): List<FacturaModelRoom>? {
        try {
        // Se hace la llamada a la API para obtener las facturas
        val response = retrofitBuilder.create(APIService::class.java).getFacturas()
        if (response.isSuccessful) {
        // Se extraen las facturas del cuerpo de la respuesta
        val facturas = response.body()?.facturas

            if (facturas.isNullOrEmpty()) {
                return emptyList()
            } else {
                return facturas
            }
        } else {
            return null
        }

    } catch (e: Exception) {
        return null
    }
}

    suspend fun getFacturasRetroMock(): List<FacturaModelRoom>?{
        try{
        val response =  retromockBuilder.create(RetroMockService::class.java ).getFacturasMock()
        if (response.isSuccessful) {
            val invoices = response.body()?.facturas
            if (invoices.isNullOrEmpty()) {
                return emptyList()
            } else {
                return invoices
            }
        } else {
            return null
        }
    } catch (e: Exception) {
        return null
    }
}
    suspend fun getDatosSmartSolarFromRetromock(): Detalles?{
        val response = retromockBuilder.create(DatosSmartSolarRetroMock::class.java).getDatosSmartSolarFromMock()
        if (response.isSuccessful && response.body() != null) {
            val detailsData = response.body()
            return detailsData
        } else{
            Log.d("DETAILS_TAB", "Ha ocurrido un error")
            return null
        }
    }
}
