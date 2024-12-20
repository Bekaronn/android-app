package com.example.newsapp

import AppDatabase
import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.api.NewsApi
import com.example.newsapp.model.Article
import com.example.newsapp.model.NewsResponse
import com.example.newsapp.model.newsMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewsViewModel(
    private val client: NewsApi,
    private val database: AppDatabase // Подключаем базу данных
) : ViewModel() {

    private val _newsListUI = MutableLiveData<NewsListUI>()
    val newsListUI: LiveData<NewsListUI> = _newsListUI

    private val _savedArticles = MutableLiveData<List<Article>>()
    val savedArticles: LiveData<List<Article>> = _savedArticles

    fun fetchNewsList() {
        _newsListUI.value = NewsListUI.Loading(true)

        client.fetchNewsList("bitcoin", "3738013b0fe84f2b8339b09f5b068900").enqueue(object : Callback<NewsResponse> {
            override fun onResponse(call: Call<NewsResponse>, response: Response<NewsResponse>) {
                if (!response.isSuccessful) {
                    _newsListUI.value = NewsListUI.Error(R.string.error_general)
                    return
                }

                val newsResponse = response.body()

                if (newsResponse != null) {
                    val articles = newsResponse.articles
                    if (articles.isNotEmpty()) {
                        val mappedArticles = articles.map(newsMapper)
                        _newsListUI.value = NewsListUI.Success(mappedArticles)
                    } else {
                        _newsListUI.value = NewsListUI.Empty
                    }

                    _newsListUI.value = NewsListUI.Loading(false)
                } else {
                    _newsListUI.value = NewsListUI.Empty
                }
            }

            override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                _newsListUI.value = NewsListUI.Error(R.string.error_general)
            }
        })
    }

    fun searchNews(query: String) {
        if (query.isEmpty()) {
            return
        }

        _newsListUI.value = NewsListUI.Loading(true)

        client.fetchNewsList(query, "3738013b0fe84f2b8339b09f5b068900").enqueue(object : Callback<NewsResponse> {
            override fun onResponse(call: Call<NewsResponse>, response: Response<NewsResponse>) {
                if (!response.isSuccessful) {
                    _newsListUI.value = NewsListUI.Error(R.string.error_general)
                    return
                }

                val newsResponse = response.body()

                if (newsResponse != null) {
                    val mappedArticles = newsResponse.articles.map(newsMapper)
                    _newsListUI.value = NewsListUI.Success(mappedArticles)
                } else {
                    _newsListUI.value = NewsListUI.Empty
                }

                _newsListUI.value = NewsListUI.Loading(false)
            }

            override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                _newsListUI.value = NewsListUI.Error(R.string.error_general)
                _newsListUI.value = NewsListUI.Loading(false)
            }
        })
    }

    fun saveArticle(article: Article) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                database.articleDao().insertArticle(article)
            }
        }
    }

    fun deleteArticle(article: Article) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                database.articleDao().deleteArticle(article)
            }
        }
    }

    fun loadSavedArticles() {
        viewModelScope.launch {
            val articles: List<com.example.newsapp.model.Article> = withContext(Dispatchers.IO) {
                database.articleDao().getAllArticles()
            }
            _savedArticles.postValue(articles)
        }
    }
}

sealed interface NewsListUI {
    data class Loading(val isLoading: Boolean) : NewsListUI
    data class Error(@StringRes val errorMessage: Int) : NewsListUI
    data class Success(val newsList: List<Article>) : NewsListUI
    data object Empty : NewsListUI
}
