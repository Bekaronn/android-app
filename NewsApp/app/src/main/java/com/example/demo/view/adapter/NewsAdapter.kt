package com.example.demo.view.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.demo.databinding.ItemNewsBinding
import com.example.demo.model.entity.Article

class NewsAdapter(
    private val onArticleClickListener: (Article) -> Unit,
    private val onChangeFavouriteState: (Article, Boolean) -> Unit
) : ListAdapter<Article, NewsAdapter.ViewHolder>(NewsItemCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        println("onBindViewHolder: $position")
        holder.bind(getItem(position))
    }

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    inner class ViewHolder(
        private val binding: ItemNewsBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(article: Article) {
            with(binding) {
                Log.d("NewsAdapter", "Binding article: ${article.title}")

                newsTitle.text = article.title
                newsDescription.text = article.description ?: "No description available"
                newsPublishedAt.text = article.publishedAt ?: "Unknown date"
                newsSource.text = article.source.name ?: "Unknown source"
                article.urlToImage?.let {
                    Glide.with(binding.root.context)
                        .load(it)
                        .into(newsImage)
                }

                saveButton.setOnClickListener {
                    onChangeFavouriteState(article, !article.isFavourite)
                }

                // Обработка клика на всю карточку
                root.setOnClickListener {
                    onArticleClickListener(article)
                }
//
//                // Обработка изменения состояния избранного
//                favouriteImageView.setOnClickListener {
//                    onChangeFavouriteState(article, !article.isFavourite)
//                }
            }
        }
    }
}