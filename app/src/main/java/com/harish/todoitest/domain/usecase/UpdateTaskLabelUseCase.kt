package com.harish.todoitest.domain.usecase

import com.harish.todoitest.domain.KResult

interface UpdateTaskLabelUseCase {
    suspend operator fun invoke(id: Long, label: String): KResult<Unit>
}