package com.harish.todoitest.data.repository

import com.harish.todoitest.data.SyncWorkerManager
import com.harish.todoitest.data.datasource.local.TaskDatabase
import com.harish.todoitest.data.datasource.local.TaskEntity
import com.harish.todoitest.domain.KResult
import com.harish.todoitest.domain.entity.Task
import com.harish.todoitest.domain.onSuccess
import com.harish.todoitest.domain.repository.TaskRepository
import com.harish.todoitest.domain.runCatchingResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class TaskRepositoryImpl @Inject constructor(
    private val db: TaskDatabase,
    private val syncWorkManager: SyncWorkerManager
): TaskRepository {
    override suspend fun fetchAll(): KResult<List<Task>> = runCatchingResult {
        db.taskDao().list()
    }

    override fun streamAll(): Flow<List<Task>> = db.taskDao().streamList()

    override suspend fun get(id: Long): KResult<Task> = runCatchingResult {
        db.taskDao().get(id)
    }

    override suspend fun create(label: String): KResult<Long> = runCatchingResult {
        TaskEntity(label)
            .let { db.taskDao().insert(it) }
    }.onSuccess { syncWorkManager.enqueueSync() }

    override suspend fun create(task: Task): KResult<Long> = runCatchingResult {
        TaskEntity(task, isNew = true)
            .let { db.taskDao().insert(it) }
    }.onSuccess { syncWorkManager.enqueueSync() }

    override suspend fun update(id: Long, task: Task): KResult<Unit> = runCatchingResult {
        TaskEntity(task, isUpdated = true)
            .let { db.taskDao().update(it) }
    }.onSuccess { syncWorkManager.enqueueSync() }

    override suspend fun delete(id: Long): KResult<Unit> = runCatchingResult {
        db.taskDao().get(id).copy(isDeleted = true)
            .let { db.taskDao().update(it) }
    }.onSuccess { syncWorkManager.enqueueSync() }

    override suspend fun syncPendingList(): KResult<List<Task>> = runCatchingResult {
        db.taskDao().pendingSyncTaskList()
    }

    override suspend fun markCompleted(id: Long, isCompleted: Boolean): KResult<Unit> = runCatchingResult {
        db.taskDao().get(id).copy(isCompleted = isCompleted, isUpdated = true)
            .let { db.taskDao().update(it) }
    }.onSuccess { syncWorkManager.enqueueSync() }
}