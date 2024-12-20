package com.example.newsapp

import AppDatabase
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.newsapp.api.ApiSource

class NewsViewModelFactory(
    private val database: AppDatabase // Передаём экземпляр базы данных
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NewsViewModel::class.java)) {
            return NewsViewModel(
                client = ApiSource.client, // API-клиент
                database = database // База данных
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
