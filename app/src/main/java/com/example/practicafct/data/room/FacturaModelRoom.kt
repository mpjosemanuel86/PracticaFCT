package com.example.practicafct.data.room

import androidx.room.Entity

// Definición de la entidad de base de datos para representar facturas
@Entity(tableName = "factura_tabla", primaryKeys = ["importeOrdenacion", "fecha"])
class FacturaModelRoom(
    // Descripción del estado de la factura
    val descEstado: String,
    // Importe de ordenación de la factura, se utilizará como una de las claves primarias
    val importeOrdenacion: Double,
    // Fecha de la factura, también se utilizará como una de las claves primarias
    val fecha: String

)