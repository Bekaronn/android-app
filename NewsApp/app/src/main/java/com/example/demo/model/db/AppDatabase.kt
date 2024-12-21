package com.example.demo.model.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.demo.model.dao.ArticleDao
import com.example.demo.model.entity.ArticleEntity

@Database(
    entities = [ArticleEntity::class],
    version = 3)
abstract class AppDatabase : RoomDatabase() {
    abstract fun articleDao(): ArticleDao
}