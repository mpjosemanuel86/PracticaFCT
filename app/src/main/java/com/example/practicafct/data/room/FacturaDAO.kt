package com.example.practicafct.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

// Interfaz que define las operaciones de acceso a datos (DAO) para la tabla de facturas
@Dao
interface FacturaDAO {

    // Obtiene todas las facturas almacenadas en la base de datos local
    @Query("SELECT * FROM factura_tabla")
    fun getAllFacturasFromRoom(): List<FacturaModelRoom>

    // Inserta una lista de facturas en la base de datos local, reemplazando cualquier registro existente en caso de conflicto
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFacturasInRoom(facturaModelRoom: List<FacturaModelRoom>)

    // Elimina todas las facturas de la base de datos local
    @Query("DELETE FROM factura_tabla")
    fun deleteAllFacturasFromRoom()
}
