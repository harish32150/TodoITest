package com.harish.todoitest.domain.usecase

import com.harish.todoitest.domain.KResult
import com.harish.todoitest.domain.entity.Task

interface SyncTaskUseCase {
    suspend operator fun invoke(id: Long): KResult<Long>
}