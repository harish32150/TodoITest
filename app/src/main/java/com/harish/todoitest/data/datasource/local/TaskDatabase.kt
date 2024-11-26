package com.harish.todoitest.data.datasource.local

import androidx.room.RoomDatabase

internal abstract class TaskDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
}