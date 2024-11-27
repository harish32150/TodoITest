package com.harish.todoitest.domain.repository

import com.harish.todoitest.domain.KResult
import com.harish.todoitest.domain.entity.Task

interface TaskRepository {
    suspend fun fetchAll(): KResult<List<Task>>

    suspend fun create(label: String): KResult<Long>

    suspend fun create(task: Task): KResult<Unit>

    suspend fun update(id: Long, task: Task): KResult<Unit>

    suspend fun delete(id: Long): KResult<Unit>
}