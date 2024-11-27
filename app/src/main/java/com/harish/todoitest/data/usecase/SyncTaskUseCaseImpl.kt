package com.harish.todoitest.data.usecase

import com.harish.todoitest.data.datasource.local.TaskDatabase
import com.harish.todoitest.data.datasource.local.TaskEntity
import com.harish.todoitest.data.datasource.remote.RestApi
import com.harish.todoitest.data.datasource.remote.TaskApiModel
import com.harish.todoitest.domain.KResult
import com.harish.todoitest.domain.map
import com.harish.todoitest.domain.usecase.SyncTaskUseCase
import javax.inject.Inject

internal class SyncTaskUseCaseImpl @Inject constructor(
    private val db: TaskDatabase,
    private val api: RestApi
): SyncTaskUseCase {
    override suspend operator fun invoke(id: Long): KResult<Long> {
        val localTask = db.taskDao().get(id)
        return when {
            localTask.isNew -> api.createTask(TaskApiModel(localTask))
                .map { db.taskDao().sync(id, TaskEntity(it)) }

            localTask.isUpdated -> api.updateTask(id, TaskApiModel(localTask))
                .map { db.taskDao().sync(id, TaskEntity(it)) }

            localTask.isDeleted -> api.deleteTask(id)
                .map { db.taskDao().delete(id) }

            else -> KResult.Error(Throwable("Nothing to sync"))
        }.map { id }
    }
}