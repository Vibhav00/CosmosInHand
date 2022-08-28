package com.example.cosmosinhand.models.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "table01")
data class DatabaseItem(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var ure: String,
    var des: String
)