package com.example.cosmosinhand.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cosmosinhand.models.apod.ApodItem
import com.example.cosmosinhand.models.database.DatabaseItem
import com.example.cosmosinhand.models.iavl.Iavl
import com.example.cosmosinhand.repository.CosmosRepository
import com.example.cosmosinhand.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class CosmosViewModel(app: Application, val cosmosRepository: CosmosRepository) :
    AndroidViewModel(app) {

    val iavlList: MutableLiveData<Resource<Iavl>> = MutableLiveData()
    val apodList: MutableLiveData<Resource<List<ApodItem>>> = MutableLiveData()
    val imagelist: MutableLiveData<Resource<List<String>>> = MutableLiveData()

    init {

        getIavlList("sun")
        getApodListini()
    }

    fun getApodList(startdate: String, endDate: String) = viewModelScope.launch {
        safeGetApodList(startdate,endDate)

    }

    private fun handleApodResponse(response: Response<List<ApodItem>>): Resource<List<ApodItem>> {
        if (response.isSuccessful) {
            response.body()?.let { it ->


                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

    fun getIavlList(searchname: String) = viewModelScope.launch {
       safeGetIavlList(searchname)
       // Log.e("vibhav", handleIavlResponse(responseiavl).data.toString())

    }

    private fun handleIavlResponse(response: Response<Iavl>): Resource<Iavl> {
        if (response.isSuccessful) {

            response.body()?.let { it ->
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

    fun getImageList(url: String) = viewModelScope.launch {
        imagelist.postValue(Resource.Loding())
        val responseimage = cosmosRepository.getImageList(url)
        imagelist.postValue(handleImageListResponse(responseimage))
    }

    private fun handleImageListResponse(response: Response<List<String>>): Resource<List<String>> {
        if (response.isSuccessful) {
            response.body()?.let { it ->
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

    fun saveIntoDatabase(databaseItem: DatabaseItem) = viewModelScope.launch {
        cosmosRepository.upsert(databaseItem)
    }

    fun getAllSavedItems() =
        cosmosRepository.getSavedItems()

    fun deleteSavedItem(databaseItem: DatabaseItem) = viewModelScope.launch {
        cosmosRepository.deleteDatabaseItem(databaseItem)
    }


    private suspend fun safeGetIavlList(searchname: String) {

        iavlList.postValue(Resource.Loding())
        try {
            if (hasInternateConnection()) {
                val response = cosmosRepository.getIavl(searchname)
                    iavlList.postValue(handleIavlResponse(response))
            }
            else
            {
               iavlList.postValue(Resource.Error("network error"))
            }

        }catch (t:Throwable)
        {
            when(t)
            {
                is IOException->iavlList.postValue(Resource.Error("ioexception") )
                else->iavlList.postValue(Resource.Error("conversion error") )
            }

        }
    }
    private suspend fun safeGetApodList(startdate: String, endDate: String) {

        apodList.postValue(Resource.Loding())
        try {

           // Log.e("vibhav_view",hasInternateConnection().toString())

            if (hasInternateConnection()) {
                val response = cosmosRepository.getApod(startdate, endDate)

                apodList.postValue(handleApodResponse(response))
            }
            else
            {
                    apodList.postValue(Resource.Error("network error"))
            }

        }catch (t:Throwable)
        {
            when(t)
            {
                      is IOException->apodList.postValue(Resource.Error("ioexception") )
                        else->apodList.postValue(Resource.Error("conversion error") )
            }

        }
    }


     fun hasInternateConnection(): Boolean {

        val connectivityManager = getApplication<CosmosApplication>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities =
                connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {

            connectivityManager.activeNetworkInfo?.run {
                return when (type) {
                    ConnectivityManager.TYPE_WIFI -> true
                    ConnectivityManager.TYPE_MOBILE -> true
                    ConnectivityManager.TYPE_ETHERNET -> true
                    else -> false
                }


            }
        }
        return false
    }
    private  fun getApodListini()
    {
        var simpleDateFormate =  SimpleDateFormat("yyyy-MM-dd")
        var sd=simpleDateFormate.format(System.currentTimeMillis())
        //Log.e("today",)
        val myCal= Calendar.getInstance()
        myCal.add(Calendar.DAY_OF_YEAR,-5)
        var ed=simpleDateFormate.format(myCal.time)
        getApodList(ed, sd)
    }

}