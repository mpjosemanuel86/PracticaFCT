package com.example.practicafct.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FacturaDAO {
    @Query("SELECT * FROM factura_tabla")
    fun getAllFacturasFromRoom(): List<FacturaModelRoom>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFacturasInRoom(facturaModelRoom: List<FacturaModelRoom>)

    @Query("DELETE FROM factura_tabla")
    fun deleteAllFacturasFromRoom()
}