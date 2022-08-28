package com.example.cosmosinhand.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.cosmosinhand.models.database.DatabaseItem


@Dao
interface DatabaseItemDaw {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(databaseItem: DatabaseItem): Long

    @Query("select * from table01")
    fun getAllItems(): LiveData<List<DatabaseItem>>

    @Delete
    suspend fun delOneItem(databaseItem: DatabaseItem)

}