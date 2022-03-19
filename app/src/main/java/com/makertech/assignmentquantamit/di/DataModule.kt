package com.makertech.assignmentquantamit.di

import android.app.Application
import androidx.room.Room
import com.google.gson.GsonBuilder
import com.makertech.assignmentquantamit.data.local.NewsDao
import com.makertech.assignmentquantamit.data.local.NewsDatabase
import com.makertech.assignmentquantamit.data.remote.NewsApi
import com.makertech.assignmentquantamit.other.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Singleton
    @Provides
    fun provideNoteApi() : NewsApi {
        val client = OkHttpClient.Builder()
            .build()
        val gson = GsonBuilder()
            .create()
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()
            .create(NewsApi::class.java)
    }

    @Provides
    @Singleton
    fun provideDatabase(app: Application) : NewsDatabase =
        Room.databaseBuilder(app, NewsDatabase::class.java, "news_database")
            .build()

    @Singleton
    @Provides
    fun provideNewsDao(database: NewsDatabase): NewsDao {
        return database.getNewsDao()
    }
}