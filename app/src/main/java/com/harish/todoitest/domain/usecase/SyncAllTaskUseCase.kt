package com.harish.todoitest.domain.usecase

import com.harish.todoitest.domain.KResult
import com.harish.todoitest.domain.map
import com.harish.todoitest.domain.repository.TaskRepository
import javax.inject.Inject

class SyncAllTaskUseCase @Inject constructor(
    private val taskRepository: TaskRepository,
    private val syncTaskUseCase: SyncTaskUseCase
) {
    suspend operator fun invoke(): KResult<Boolean> =
        taskRepository.syncPendingList()
            .map { taskList ->
                /* check if all synced, return as flag to schedule retry */
                taskList.takeIf { it.isNotEmpty() }
                    ?.map { syncTaskUseCase.invoke(it.id) }
                    ?.all { it is KResult.Success }
                    ?: true
            }
}