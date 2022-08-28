package com.example.cosmosinhand.repository

import com.example.cosmosinhand.api.RetrofitInstance
import com.example.cosmosinhand.database.DatabaseMain

import com.example.cosmosinhand.models.apod.ApodItem
import com.example.cosmosinhand.models.database.DatabaseItem
import com.example.cosmosinhand.models.iavl.Iavl
import retrofit2.Response

class CosmosRepository(var db: DatabaseMain) {

    suspend fun getApod(startdate: String, endDate: String)= RetrofitInstance.api.getApod(startdate, endDate)
    suspend fun getIavl(searchname: String) = RetrofitInstance.api.getIavl(searchname)
    suspend fun getImageList(url: String) = RetrofitInstance.api.getImages(url)
    suspend fun upsert(databaseItem: DatabaseItem) = db.getDatabaseDaw().upsert(databaseItem)
    fun getSavedItems() = db.getDatabaseDaw().getAllItems()
    suspend fun deleteDatabaseItem(databaseItem: DatabaseItem) = db.getDatabaseDaw().delOneItem(databaseItem)
}