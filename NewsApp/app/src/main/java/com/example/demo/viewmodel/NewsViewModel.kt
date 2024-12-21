package com.example.demo.viewmodel

import android.util.Log
import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.demo.R
import com.example.demo.model.api.NewsApi
import com.example.demo.model.dao.ArticleDao
import com.example.demo.model.entity.Article
import com.example.demo.model.entity.ArticleEntity
import com.example.demo.model.entity.NewsResponse
import com.example.demo.model.entity.newsMapper
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewsViewModel(
    private val client: NewsApi,
    private val articleDao: ArticleDao
) : ViewModel() {

    private val _newsListUI = MutableLiveData<NewsListUI>()
    val newsListUI: LiveData<NewsListUI> = _newsListUI

    fun changeFavouriteState(article: Article, isFavourite: Boolean) {
        viewModelScope.launch {
            try {
                if (isFavourite) {
                    articleDao.insert(ArticleEntity.from(article))

                    _newsListUI.value = NewsListUI.NewsInserted(article.copy(isFavourite = true))
                }
            } catch (e: Exception) {
                println("NewsInsertException: $e")

                _newsListUI.value = NewsListUI.NewsIsAlreadyFavourite
            }
        }
    }

    fun fetchNewsList() {
        _newsListUI.value = NewsListUI.Loading(true)

        client.fetchNewsList("bitcoin", "3738013b0fe84f2b8339b09f5b068900").enqueue(object :
            Callback<NewsResponse> {
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

        client.fetchNewsList(query, "3738013b0fe84f2b8339b09f5b068900").enqueue(object :
            Callback<NewsResponse> {
            override fun onResponse(call: Call<NewsResponse>, response: Response<NewsResponse>) {
                if (!response.isSuccessful) {
                    _newsListUI.value = NewsListUI.Error(R.string.error_general)
                    return
                }

                val newsResponse = response.body()

                Log.d("searchNews", "Search result: $newsResponse")

                if (newsResponse != null) {
                    val mappedArticles = newsResponse.articles.map(newsMapper)
                    _newsListUI.value = NewsListUI.Success(mappedArticles)
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
    data class Success(val newsList: List<Article>) : NewsListUI
    data object Empty : NewsListUI
    data class NewsInserted(val article: Article) : NewsListUI
    data object NewsIsAlreadyFavourite : NewsListUI
}
