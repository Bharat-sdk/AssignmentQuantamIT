package com.makertech.assignmentquantamit.data.remote

import com.makertech.assignmentquantamit.BuildConfig
import com.makertech.assignmentquantamit.data.models.NewsResponse
import com.makertech.assignmentquantamit.other.Constants.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {
    @GET("top-headlines?apiKey=$API_KEY&category=technology&country=us")
    suspend fun getNews(): Response<NewsResponse>
}