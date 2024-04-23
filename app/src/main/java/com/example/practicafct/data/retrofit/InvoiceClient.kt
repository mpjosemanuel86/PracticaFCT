package com.example.practicafct.data.retrofit

import com.example.practicafct.Facturas
import com.example.practicafct.FacturasList
import retrofit2.Response
import retrofit2.http.GET

interface InvoiceClient {
    @GET("facturas")
    suspend fun getDataFromAPI(): Response<FacturasList>
}

