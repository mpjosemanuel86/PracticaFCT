package com.example.practicafct.data.room

import androidx.room.Entity

@Entity(tableName = "factura_tabla", primaryKeys = ["importeOrdenacion", "fecha"])
class FacturaModelRoom(
    val descEstado: String,
    val importeOrdenacion: Double,
    val fecha: String
)