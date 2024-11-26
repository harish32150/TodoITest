package com.harish.todoitest.data.di

import android.content.Context
import androidx.room.Room
import com.harish.todoitest.data.datasource.local.TaskDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    internal fun provideTaskDatabase(@ApplicationContext context: Context): TaskDatabase =
        Room.databaseBuilder(context, TaskDatabase::class.java, "task_db")
            .fallbackToDestructiveMigration()
            .build()
}