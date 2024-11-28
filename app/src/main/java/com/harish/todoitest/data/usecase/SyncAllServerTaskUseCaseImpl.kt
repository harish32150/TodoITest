package com.harish.todoitest.data.usecase

import com.harish.todoitest.data.datasource.local.TaskDatabase
import com.harish.todoitest.data.datasource.local.TaskEntity
import com.harish.todoitest.data.datasource.remote.RestApi
import com.harish.todoitest.domain.KResult
import com.harish.todoitest.domain.map
import com.harish.todoitest.domain.usecase.SyncAllServerTaskUseCase
import javax.inject.Inject

internal class SyncAllServerTaskUseCaseImpl @Inject constructor(
    private val api: RestApi,
    private val db: TaskDatabase
): SyncAllServerTaskUseCase {
    override suspend fun invoke(): KResult<Unit> {
        return api.taskList()
            .map { taskList -> taskList.map { TaskEntity(it) } }
            .map { db.taskDao().insertAll(it) }
    }
}