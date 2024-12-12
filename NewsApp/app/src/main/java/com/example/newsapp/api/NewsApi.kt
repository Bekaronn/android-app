package com.example.newsapp.api

import com.example.newsapp.model.NewsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {
    @GET("v2/everything")
    fun fetchNewsList(@Query("q") query: String, @Query("apiKey") apiKey: String): Call<NewsResponse>
}
