package com.example.demo.model.entity

import java.util.UUID

data class Source(
    val id: String?,
    val name: String
)

data class Article(
    val id: String = UUID.randomUUID().toString(),
    val source: Source,
    val author: String?,
    val title: String,
    val description: String?,
    val url: String,
    val urlToImage: String?,
    val publishedAt: String,
    val content: String?,
    val language: String?,
    val sourceCountry: String?,
    val isFavourite: Boolean = false
)

data class News(
    val status: String,
    val totalResults: Int,
    val articles: List<Article>
)

val newsMapper: (Article) -> Article = { article ->
    Article(
        id = UUID.randomUUID().toString(),
        source = article.source?.let {
            Source(
                id = it.id ?: UUID.randomUUID().toString(),
                name = it.name
            )
        } ?: Source(UUID.randomUUID().toString(), "Unknown Source"),
        author = article.author ?: "Unknown Author",
        title = article.title ?: "Untitled Article",
        description = article.description ?: "No description available",
        url = article.url ?: "No URL provided",
        urlToImage = article.urlToImage ?: "No image available",
        publishedAt = article.publishedAt ?: "No publish date available",
        content = article.content ?: "No content available",
        language = article.language ?: "Unknown Language",
        sourceCountry = article.sourceCountry ?: "Unknown Country"
    )
}

val articleEntityMapper: (ArticleEntity) -> Article = { response ->
    Article(
        id = response.id,
        source = Source(
            id = null,
            name = response.sourceName
        ),
        author = response.author ?: "Unknown Author",
        title = response.title,
        description = response.description ?: "No description available",
        url = response.url,
        urlToImage = response.imageUrl ?: "No image available",
        publishedAt = response.publishedAt,
        content = response.content ?: "No content available",
        language = "Unknown Language",
        sourceCountry = "Unknown Country"
    )
}