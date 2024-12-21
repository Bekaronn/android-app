package com.example.demo.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.demo.model.dao.ArticleDao
import com.example.demo.model.entity.Article
import com.example.demo.model.entity.ArticleEntity
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

    fun removeFromFavourite(article: Article) {
        viewModelScope.launch {
            try {
                newsDao.delete(ArticleEntity.from(article))

                _newsDetailsUI.value = NewsDetailsUI.NewsRemoved(article.copy(isFavourite = false))
            } catch (e: Exception) {
                Log.e("NewsDetailsViewModel", "Error removing article from favourites: ${e.message}")
                _newsDetailsUI.value = NewsDetailsUI.NewsChangeError
            }
        }
    }
}

sealed interface NewsDetailsUI {
    data class Success(val newsList: List<Article>) : NewsDetailsUI
    data class NewsRemoved(val article: Article) : NewsDetailsUI
    object NewsChangeError : NewsDetailsUI
}
