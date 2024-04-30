package com.example.practicafct.data.retrofit

import com.example.practicafct.core.retrofit.RetrofitHelper
import com.example.practicafct.data.room.FacturaModelRoom


class FacturaService {


    private val retrofitBuilder = RetrofitHelper.getRetrofit()


    suspend fun getFacturas(): List<FacturaModelRoom>? {

        val response = retrofitBuilder.create(APIService::class.java).getFacturas()
        val facturas = response.body()?.facturas
        return facturas


    }
}
