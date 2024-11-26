package com.harish.todoitest.data.datasource.local

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

internal interface TaskDao {
    @Query("SELECT * FROM tasks WHERE isDeleted = 0 ORDER BY id ASC")
    suspend fun taskList(): List<TaskEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(task: TaskEntity)

    @Update
    suspend fun update(task: TaskEntity)

    @Delete
    suspend fun delete(task: TaskEntity)

    @Query("SELECT * FROM tasks WHERE isNew = 1 OR isUpdated = 1 OR isDeleted = 1")
    suspend fun pendingSyncTaskList(): List<TaskEntity>

    @Query("UPDATE tasks SET isNew = 0, isUpdated = 0, isDeleted = 0 WHERE id IN (:taskIds)")
    suspend fun markSynced(taskIds: List<Int>)

    @Query("UPDATE tasks SET isNew = 0, isUpdated = 0, isDeleted = 0 WHERE id = :taskId")
    suspend fun markSynced(taskId: Int)
}