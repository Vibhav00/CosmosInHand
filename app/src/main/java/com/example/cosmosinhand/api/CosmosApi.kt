package com.example.cosmosinhand.api

import com.example.cosmosinhand.models.apod.ApodItem
import com.example.cosmosinhand.models.iavl.Iavl
import com.example.cosmosinhand.util.Constants.Companion.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface CosmosApi {

    @GET("https://api.nasa.gov/planetary/apod")
    suspend fun getApod(
        @Query("start_date")
        startdate: String = "2022-01-01",
        @Query("end_date")
        endDate: String = "2022-01-05",
        @Query("api_key")
        apikey: String = API_KEY

    ): Response<List<ApodItem>>


    @GET("https://images-api.nasa.gov/search")

    suspend fun getIavl(
        @Query("q")
        searchname: String = "sun"
    ): Response<Iavl>

    @GET()
    suspend fun getImages(
        @Url()
        url: String
    ): Response<List<String>>

}