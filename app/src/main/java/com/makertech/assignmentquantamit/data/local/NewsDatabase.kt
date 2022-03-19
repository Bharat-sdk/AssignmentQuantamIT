package com.makertech.assignmentquantamit.data.local

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.makertech.assignmentquantamit.data.local.entities.NewsArticleEntity
import com.makertech.assignmentquantamit.data.models.Article

@Database(
    entities = [NewsArticleEntity::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class NewsDatabase : RoomDatabase() {

    abstract fun getNewsDao(): NewsDao

}