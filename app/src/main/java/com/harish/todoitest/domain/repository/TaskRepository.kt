package com.harish.todoitest.domain.repository

import com.harish.todoitest.domain.KResult
import com.harish.todoitest.domain.entity.Task
import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    suspend fun fetchAll(): KResult<List<Task>>

    fun streamAll(): Flow<List<Task>>

    suspend fun get(id: Long): KResult<Task>

    suspend fun create(label: String): KResult<Long>

    suspend fun create(task: Task): KResult<Long>

    suspend fun update(id: Long, task: Task): KResult<Unit>

    suspend fun delete(id: Long): KResult<Unit>

    suspend fun syncPendingList(): KResult<List<Task>>

    suspend fun markCompleted(id: Long, isCompleted: Boolean): KResult<Unit>
}