package com.example.demo.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "articles")
data class ArticleEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String?,
    val author: String?,
    val content: String?,
    val url: String,
    @ColumnInfo("source_name") val sourceName: String,
    @ColumnInfo("image_url") val imageUrl: String?,
    @ColumnInfo("published_at") val publishedAt: String
) {

    companion object {
        fun from(article: Article) = ArticleEntity(
            id = article.id,
            title = article.title,
            description = article.description,
            author = article.author,
            content = article.content,
            url = article.url,
            sourceName = article.source.name,
            imageUrl = article.urlToImage,
            publishedAt = article.publishedAt
        )
    }
}
