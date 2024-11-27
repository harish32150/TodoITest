package com.harish.todoitest.data.repository

import com.harish.todoitest.data.datasource.local.TaskDatabase
import com.harish.todoitest.data.datasource.local.TaskEntity
import com.harish.todoitest.domain.KResult
import com.harish.todoitest.domain.entity.Task
import com.harish.todoitest.domain.repository.TaskRepository
import com.harish.todoitest.domain.runCatchingResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class TaskRepositoryImpl @Inject constructor(
    private val db: TaskDatabase
): TaskRepository {
    override suspend fun fetchAll(): KResult<List<Task>> = runCatchingResult {
        db.taskDao().list()
    }

    override fun streamAll(): Flow<List<Task>> = db.taskDao().streamList()

    override suspend fun create(label: String): KResult<Long> = runCatchingResult {
        TaskEntity(label)
            .let { db.taskDao().insert(it) }
    }

    override suspend fun create(task: Task): KResult<Unit> = runCatchingResult {
        TaskEntity(task, isNew = true)
            .let { db.taskDao().insert(it) }
    }

    override suspend fun update(id: Long, task: Task): KResult<Unit> = runCatchingResult {
        TaskEntity(task, isUpdated = true)
            .let { db.taskDao().update(it) }
    }

    override suspend fun delete(id: Long): KResult<Unit> = runCatchingResult {
        db.taskDao().get(id).copy(isDeleted = true)
            .let { db.taskDao().update(it) }
    }
}