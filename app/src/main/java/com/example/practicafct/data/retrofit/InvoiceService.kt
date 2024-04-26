package com.example.practicafct.data.retrofit

import android.util.Log
import com.example.practicafct.Facturas
import com.example.practicafct.core.retrofit.RetrofitHelper

// Clase que interactúa con un servicio de facturación mediante Retrofit
class InvoiceService {

    // Obtiene una instancia preconfigurada de Retrofit
    private val retrofitBuilder = RetrofitHelper.getRetrofit()

    // Método para obtener datos de facturas del servicio
    suspend fun getDataFromAPI(): List<Facturas>? {
        // Realiza una solicitud HTTP al servidor para obtener datos de facturas
        val response = retrofitBuilder.create(InvoiceClient::class.java).getDataFromAPI()

        // Registra el código de respuesta en los logs
        Log.d("InvoiceService", "Código de respuesta: ${response.code()}")

        // Si la respuesta es exitosa (código de respuesta en el rango)
        if (response.isSuccessful) {
            // Extrae las facturas de la respuesta
            val invoices = response.body()?.facturas

            // Registra las facturas en los logs
            Log.d("InvoiceService", "Facturas: $invoices")

            // Si la lista de facturas está vacía o nula, devuelve una lista vacía
            if (invoices.isNullOrEmpty()) {
                return emptyList()
            } else {
                return invoices
            }
        } else {
            // Si la respuesta no fue exitosa, registra el cuerpo del error en los logs
            val errorBody = response.errorBody()?.string()
            Log.d("InvoiceService", "Respuesta no exitosa: $errorBody")
            return null
        }
    }
}
