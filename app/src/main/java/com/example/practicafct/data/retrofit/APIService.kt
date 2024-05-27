package com.example.practicafct.data.retrofit

import co.infinum.retromock.meta.Mock
import co.infinum.retromock.meta.MockCircular
import co.infinum.retromock.meta.MockResponse
import co.infinum.retromock.meta.MockResponses
import com.example.practicafct.data.retrofit.network.Detalles

import com.example.practicafct.data.retrofit.reponse.FacturasList
import com.example.practicafct.data.room.FacturaModelRoom
import retrofit2.Response
import retrofit2.http.GET

// Interfaz que define los métodos para realizar solicitudes HTTP al servicio de facturación
interface APIService {
    // Método GET para obtener datos de facturas del servidor remoto
    @GET("facturas")
    suspend fun getFacturas(): Response<FacturasList>
}

interface RetroMockService {
    @Mock
    @MockResponses(MockResponse(body = "mock.json"))
    @GET("/")
        suspend fun getFacturasMock(): Response<FacturasList>

}

interface DatosSmartSolarRetroMock {
    @Mock
    @MockResponses(
        MockResponse(body = "SmartSolar.json")
    )
    @MockCircular
    @GET("resources")
    suspend fun getDatosSmartSolarFromMock(): Response<Detalles>
}



