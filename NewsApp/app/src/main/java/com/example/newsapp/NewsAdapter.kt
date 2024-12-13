package com.example.newsapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsapp.databinding.ItemNewsBinding
import com.example.newsapp.model.Article

class NewsAdapter : ListAdapter<Article, NewsAdapter.ViewHolder>(NewsItemCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(
        private val binding: ItemNewsBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(article: Article) {
            with(binding) {
                // Заголовок новости
                newsTitle.text = article.title
                // Краткое описание
                newsDescription.text = article.description ?: "No description available"
                // Дата публикации
                newsPublishedAt.text = article.publishedAt ?: "Unknown date"
                // Источник
                newsSource.text = article.source.name ?: "Unknown source"
                // Изображение новости (если доступно)
                article.urlToImage?.let {
                    // Загрузите изображение с помощью библиотеки, например, Glide
                    Glide.with(binding.root.context)
                        .load(it)
                        .into(newsImage)
                }
            }
        }
    }
}
