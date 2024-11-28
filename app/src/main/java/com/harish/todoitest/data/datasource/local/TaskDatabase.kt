package com.harish.todoitest.data.datasource.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import java.util.Date

@Database(version = 2, entities = [TaskEntity::class])
@TypeConverters(Converters::class)
internal abstract class TaskDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
}

private class Converters {

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}