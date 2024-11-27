package com.harish.todoitest.data.datasource.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(version = 1, entities = [TaskEntity::class])
internal abstract class TaskDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
}