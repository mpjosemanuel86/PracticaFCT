package com.example.practicafct.data.room

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.practicafct.MyApplication

@Database(entities = [FacturaModelRoom::class], version = 1, exportSchema = false)
abstract class FacturaDatabase: RoomDatabase(){

    abstract fun getFacturaDao(): FacturaDAO

    companion object{
        private var DB_INSTANCE: FacturaDatabase?=null

        fun getAppDBInstance(): FacturaDatabase{
            if (DB_INSTANCE == null){
                DB_INSTANCE = Room.databaseBuilder(MyApplication.context, FacturaDatabase::class.java, "factura_database")
                    .allowMainThreadQueries()
                    .build()
            }
            return DB_INSTANCE!!
        }

    }

}