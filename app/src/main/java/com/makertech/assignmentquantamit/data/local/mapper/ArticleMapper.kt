package com.makertech.assignmentquantamit.data.local.mapper

import com.makertech.assignmentquantamit.data.local.entities.NewsArticleEntity
import com.makertech.assignmentquantamit.data.models.Article

fun List<Article>.toNewsEntityList(): List<NewsArticleEntity> {
    return map {
        NewsArticleEntity(
            author = it.author,
            title = it.title,
            description = it.description,
            url = it.url,
            urlToImage = it.urlToImage,
            publishedAt = it.publishedAt,
            content = it.content,
            source = it.source.name
        )
    }
}