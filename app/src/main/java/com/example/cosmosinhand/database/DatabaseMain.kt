package com.example.cosmosinhand.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.cosmosinhand.models.database.DatabaseItem

@Database(entities = [DatabaseItem::class], version = 1)
abstract class DatabaseMain : RoomDatabase() {


    abstract fun getDatabaseDaw(): DatabaseItemDaw

    companion object {
        @Volatile
        private var instance: DatabaseMain? = null

        private var LOCK = Any()
        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createDatabase(context).also {
                instance = it
            }
        }


        private fun createDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            DatabaseMain::class.java,
            "database_main.db"
        )
            .build()
    }


}