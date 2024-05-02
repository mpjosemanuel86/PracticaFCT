package com.example.practicafct.data.room

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.practicafct.MyApplication

// Definición de la base de datos Room que contiene la tabla de facturas
@Database(entities = [FacturaModelRoom::class], version = 1, exportSchema = false)
abstract class FacturaDatabase: RoomDatabase(){

    // Método abstracto para obtener el DAO (Data Access Object) asociado a la tabla de facturas
    abstract fun getFacturaDao(): FacturaDAO

    // Singleton para obtener una instancia única de la base de datos
    companion object{
        private var DB_INSTANCE: FacturaDatabase?=null


        // Método estático para obtener una instancia de la base de datos
        fun getAppDBInstance(): FacturaDatabase{
            // Si la instancia de la base de datos es nula, se crea una nueva instancia
            if (DB_INSTANCE == null){
                DB_INSTANCE = Room.databaseBuilder(MyApplication.context, FacturaDatabase::class.java, "factura_database")
                    .allowMainThreadQueries()// Permitir consultas en el hilo principal (solo para pruebas)
                    .build()
            }
            // Se devuelve la instancia de la base de datos
            return DB_INSTANCE!!
        }

    }

}