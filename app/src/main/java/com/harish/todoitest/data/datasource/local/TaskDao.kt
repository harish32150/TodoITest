package com.harish.todoitest.data.datasource.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
internal interface TaskDao {
    @Query("SELECT * FROM tasks WHERE id = :id")
    suspend fun get(id: Long): TaskEntity

    @Query("SELECT * FROM tasks WHERE isDeleted = 0 ORDER BY id DESC")
    suspend fun list(): List<TaskEntity>

    @Query("SELECT * FROM tasks WHERE isDeleted = 0 ORDER BY id DESC")
    fun streamList(): Flow<List<TaskEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(task: TaskEntity): Long

    @Update
    suspend fun update(task: TaskEntity)

    @Query("DELETE FROM tasks WHERE id = :id")
    suspend fun delete(id: Long)

    @Query("SELECT * FROM tasks WHERE isNew = 1 OR isUpdated = 1 OR isDeleted = 1")
    suspend fun pendingSyncTaskList(): List<TaskEntity>

    @Transaction
    suspend fun sync(localId: Long, task: TaskEntity): TaskEntity {
        delete(id = localId)
        insert(task)
        return get(task.id)
    }
}