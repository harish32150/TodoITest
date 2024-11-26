package com.harish.todoitest.data.repository

import com.harish.todoitest.domain.KResult
import com.harish.todoitest.domain.entity.Task
import com.harish.todoitest.domain.repository.TaskRepository
import javax.inject.Inject

internal class TaskRepositoryImpl @Inject constructor(): TaskRepository {
    override suspend fun fetchAll(): KResult<List<Task>> {
        TODO("Not yet implemented")
    }

    override suspend fun create(task: Task): KResult<Task> {
        TODO("Not yet implemented")
    }

    override suspend fun update(id: Long, task: Task): KResult<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun delete(id: Long): KResult<Unit> {
        TODO("Not yet implemented")
    }
}