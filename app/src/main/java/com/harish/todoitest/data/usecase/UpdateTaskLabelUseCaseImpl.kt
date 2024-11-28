package com.harish.todoitest.data.usecase

import com.harish.todoitest.data.SyncWorkerManager
import com.harish.todoitest.data.datasource.local.TaskDatabase
import com.harish.todoitest.domain.KResult
import com.harish.todoitest.domain.onSuccess
import com.harish.todoitest.domain.runCatchingResult
import com.harish.todoitest.domain.usecase.UpdateTaskLabelUseCase
import javax.inject.Inject

internal class UpdateTaskLabelUseCaseImpl @Inject constructor(
    private val db: TaskDatabase,
    private val syncWorkManager: SyncWorkerManager
): UpdateTaskLabelUseCase {
    override suspend operator fun invoke(id: Long, label: String): KResult<Unit> = runCatchingResult {
        db.taskDao().get(id).copy(label = label, isUpdated = true)
            .let { db.taskDao().update(it) }
    }.onSuccess { syncWorkManager.enqueueSync() }
}