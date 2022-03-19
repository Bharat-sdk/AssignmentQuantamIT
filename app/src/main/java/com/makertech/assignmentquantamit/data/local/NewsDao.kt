package com.makertech.assignmentquantamit.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.makertech.assignmentquantamit.data.local.entities.NewsArticleEntity
import com.makertech.assignmentquantamit.data.models.Article
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsDao {
    @Insert
    fun insert(articles: List<NewsArticleEntity>)

    @Query("DELETE FROM news_article")
    fun deleteAllArticles()

    @Transaction
    fun deleteAndInsertArticles(articles: List<NewsArticleEntity>) {
        deleteAllArticles()
        insert(articles)
    }

    @Query("SELECT * from news_article")
    fun getNewsArticles(): Flow<List<NewsArticleEntity>>

    @Query("SELECT * FROM news_article WHERE title LIKE :searchQuery")
    fun searchDatabase(searchQuery: String): Flow<List<NewsArticleEntity>>
}