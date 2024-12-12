package com.example.newsapp

import android.util.Log
import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.newsapp.api.NewsApi
import com.example.newsapp.model.News
import com.example.newsapp.model.NewsResponse
import com.example.newsapp.model.newsMapper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewsViewModel(
    private val client: NewsApi
) : ViewModel() {

    private val _newsListUI = MutableLiveData<NewsListUI>()
    val newsListUI: LiveData<NewsListUI> = _newsListUI

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
                    // Применяем маппинг к объекту NewsResponse и его списку articles
                    _newsListUI.value = NewsListUI.Success(newsMapper(newsResponse)) // Используем маппер для конвертации
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

                val newsList = response.body()

                Log.d("searchNews", "Search result: $newsList")

                if (newsList != null) {
                    _newsListUI.value = NewsListUI.Success(newsList.map(newsMapper))
                } else {
                    _newsListUI.value = NewsListUI.Empty
                }
                _newsListUI.value = NewsListUI.Loading(false)
            }

            override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                Log.e("searchNews", "Request failed: ${t.message}", t)
                _newsListUI.value = NewsListUI.Error(R.string.error_general)
                _newsListUI.value = NewsListUI.Loading(false)
            }
        })
    }
}

sealed interface NewsListUI {
    data class Loading(val isLoading: Boolean) : NewsListUI
    data class Error(@StringRes val errorMessage: Int) : NewsListUI
    data class Success(val newsList: List<News>) : NewsListUI
    data object Empty : NewsListUI
}
