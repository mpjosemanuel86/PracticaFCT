package com.example.practicafct.data.retrofit.network

import com.example.practicafct.data.room.DatosSmartSolarRoom

data class Detalles(
    val cau: String,
    val estadoSolicitud: String,
    val tipoAutoconsumo: String,
    val compensacionExcedentes: String,
    val potenciaInstalacion: String
) {
    fun asDatosSmartSolarModelRoom(): DatosSmartSolarRoom {


        return DatosSmartSolarRoom(
            cau,
            estadoSolicitud,
            tipoAutoconsumo,
            compensacionExcedentes,
            potenciaInstalacion
        )
    }
}