package com.example.practicafct.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface DatosSmartSolarDAO {
    @Query("SELECT * FROM tablaDetallesSmartSolar")
    fun getDatosFromRoom() : DatosSmartSolarRoom

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDatosInRoom(detailsSmartSolarRoom: DatosSmartSolarRoom)

    @Query("DELETE FROM tablaDetallesSmartSolar")
    fun deleteDatossFromRoom()
}