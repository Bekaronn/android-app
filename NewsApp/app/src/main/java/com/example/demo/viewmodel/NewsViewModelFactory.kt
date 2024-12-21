package com.example.demo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.demo.App
import com.example.demo.model.datasource.ApiSource

class NewsViewModelFactory : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NewsViewModel(
            client = ApiSource.client,
            articleDao = App.database.articleDao()
        ) as T
    }

}