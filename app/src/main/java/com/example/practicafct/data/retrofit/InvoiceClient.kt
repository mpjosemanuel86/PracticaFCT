package com.example.practicafct.data.retrofit

import com.example.practicafct.FacturasList
import retrofit2.Response
import retrofit2.http.GET

// Interfaz que define los métodos para realizar solicitudes HTTP al servicio de facturación
interface InvoiceClient {
    // Método GET para obtener datos de facturas del servidor remoto
    @GET("facturas")
    suspend fun getDataFromAPI(): Response<FacturasList>
}
