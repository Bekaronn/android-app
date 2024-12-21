package com.example.demo.view.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.demo.model.entity.Article

class NewsItemCallBack : DiffUtil.ItemCallback<Article>() {
    override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
        return oldItem == newItem
    }
}

