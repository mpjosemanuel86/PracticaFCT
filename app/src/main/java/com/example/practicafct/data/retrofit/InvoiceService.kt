package com.example.practicafct.data.retrofit

import android.util.Log
import com.example.practicafct.Facturas
import com.example.practicafct.core.retrofit.RetrofitHelper

class InvoiceService {

    private val retrofitBuilder = RetrofitHelper.getRetrofit()

    suspend fun getDataFromAPI(): List<Facturas>? {
        val response = retrofitBuilder.create(InvoiceClient::class.java).getDataFromAPI()
        Log.d("InvoiceService", "Código de respuesta: ${response.code()}")
        if (response.isSuccessful) {
            val invoices = response.body()?.facturas
            Log.d("InvoiceService", "Facturas: $invoices")
            if (invoices.isNullOrEmpty()) {
                return emptyList()
            } else {
                return invoices
            }
        } else {
            val errorBody = response.errorBody()?.string()
            Log.d("InvoiceService", "Respuesta no exitosa: $errorBody")
            return null
        }
    }
}

