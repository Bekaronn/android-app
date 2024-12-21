package com.example.demo.model.api

import com.example.demo.model.entity.NewsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {
    @GET("v2/top-headlines")
    suspend fun fetchTopHeadlines(
        @Query("country") country: String,
        @Query("category") category: String? = null,
        @Query("apiKey") apiKey: String
    ): NewsResponse

    @GET("v2/everything")
    fun fetchNewsList(@Query("q") query: String, @Query("apiKey") apiKey: String): Call<NewsResponse>
}