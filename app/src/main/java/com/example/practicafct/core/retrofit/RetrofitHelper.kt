package com.example.practicafct.core.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// Este helper proporciona una instancia preconfigurada de Retrofit
object RetrofitHelper {

    // Función para obtener una instancia de Retrofit configurada
    // con la URL base del servidor y el convertidor Gson
    fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://viewnextandroid2.wiremockapi.cloud/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}