package com.example.practicafct.data.retrofit

import com.example.practicafct.data.retrofit.reponse.FacturasList
import retrofit2.Response
import retrofit2.http.GET

// Interfaz que define los métodos para realizar solicitudes HTTP al servicio de facturación
interface APIService {
    // Método GET para obtener datos de facturas del servidor remoto
    @GET("facturas")
    suspend fun getFacturas(): Response<FacturasList>
}
