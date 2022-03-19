package com.makertech.assignmentquantamit.ui.news

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.makertech.assignmentquantamit.data.local.entities.NewsArticleEntity
import com.makertech.assignmentquantamit.data.repository.NewsRepositoryImpl
import com.makertech.assignmentquantamit.other.State
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
   val  repository: NewsRepositoryImpl
) : ViewModel() {
    private val newsArticlesLiveData = repository.getNews().asLiveData()

    fun getNewsArticles(): LiveData<State<List<NewsArticleEntity>>> {
        return newsArticlesLiveData
    }

    fun searchDatabase(searchQuery: String): LiveData<List<NewsArticleEntity>> {
        return repository.searchDatabase(searchQuery).asLiveData()
    }
}