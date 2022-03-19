package com.makertech.assignmentquantamit.data.repository

import com.makertech.assignmentquantamit.data.local.NewsDao
import com.makertech.assignmentquantamit.data.local.entities.NewsArticleEntity
import com.makertech.assignmentquantamit.data.local.mapper.toNewsEntityList
import com.makertech.assignmentquantamit.data.remote.NewsApi
import com.makertech.assignmentquantamit.other.Resource
import com.makertech.assignmentquantamit.other.State
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

class NewsRepositoryImpl @Inject constructor(val newsApi: NewsApi, val newsDao: NewsDao)  {

     fun getNews(): Flow<State<List<NewsArticleEntity>>> = flow {
        emit(State.loading<List<NewsArticleEntity>>())

        val newsArticles = try {
            newsApi.getNews().body()?.articles?.toNewsEntityList() ?: emptyList()
        } catch (ex: Exception) {
            emptyList()
        }

        if (newsArticles.isNotEmpty()) newsDao.deleteAndInsertArticles(newsArticles)

        val cachedArticles = newsDao.getNewsArticles()
        emitAll(cachedArticles.map { State.success(it) })
    }.flowOn(Dispatchers.IO)

    fun searchDatabase(searchQuery: String): Flow<List<NewsArticleEntity>> {
        return newsDao.searchDatabase(searchQuery)
    }

}