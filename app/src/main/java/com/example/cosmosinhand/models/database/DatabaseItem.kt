package com.example.cosmosinhand.models.database

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * this is the database item that are going to be saved in database (tables) ,i.e "tableo1"
 */
@Entity(tableName = "table01")
data class DatabaseItem(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var ure: String,
    var des: String
)