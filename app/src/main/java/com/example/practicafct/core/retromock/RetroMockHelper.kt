package com.example.practicafct.core.retromock

import co.infinum.retromock.Retromock
import retrofit2.Retrofit


object RetroMockHelper {
    fun getRetromock(retrofit: Retrofit): Retromock {
        return Retromock.Builder()
            .retrofit(retrofit) //Introduce el enlace de la API aquí.
            .defaultBodyFactory(ResourceBodyFactory())
            .build()
    }
}