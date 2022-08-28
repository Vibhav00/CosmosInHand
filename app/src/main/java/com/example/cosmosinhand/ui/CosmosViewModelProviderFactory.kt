package com.example.cosmosinhand.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.cosmosinhand.repository.CosmosRepository

class CosmosViewModelProviderFactory(
    val app: Application, val cosmosRepository: CosmosRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CosmosViewModel(app,cosmosRepository) as T
    }


}