package com.harish.todoitest.domain.usecase

import com.harish.todoitest.domain.KResult

interface SyncAllServerTaskUseCase {
    suspend operator fun invoke(): KResult<Unit>
}