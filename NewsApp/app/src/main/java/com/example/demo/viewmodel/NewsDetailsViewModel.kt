package com.example.demo.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.demo.model.dao.ArticleDao
import com.example.demo.model.entity.Article
import com.example.demo.model.entity.articleEntityMapper
import kotlinx.coroutines.launch

class NewsDetailsViewModel(
    private val newsDao: ArticleDao,
) : ViewModel() {

    private val _newsDetailsUI = MutableLiveData<NewsDetailsUI>()
    val newsDetailsUI: LiveData<NewsDetailsUI> = _newsDetailsUI

    fun fetchFavouriteNewsList() {
        viewModelScope.launch {
            try {
                Log.d("NewsDetailsViewModel", "Fetching news from DAO...")
                val newsList = newsDao.getAll()  // Запрос в базу данных

                Log.d("NewsDetailsViewModel", "News fetched: ${newsList.size} items")

                _newsDetailsUI.value = NewsDetailsUI.Success(
                    newsList.map(articleEntityMapper)
                )
            } catch (e: Exception) {
                Log.e("NewsDetailsViewModel", "Error fetching news: ${e.message}")
            }
        }
    }
}

sealed interface NewsDetailsUI {
    data class Success(val newsList: List<Article>) : NewsDetailsUI
}
