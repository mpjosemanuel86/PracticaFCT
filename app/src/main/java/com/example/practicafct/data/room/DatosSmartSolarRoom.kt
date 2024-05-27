package com.example.practicafct.data.room

import androidx.room.Entity

@Entity(tableName = "tablaDetallesSmartSolar", primaryKeys = ["cau"])
class DatosSmartSolarRoom (
    val cau: String,
    val estadoSolicitud: String,
    val tipoAutoconsumo: String,
    val compensacionExcedentes: String,
    val potenciaInstalacion: String

)