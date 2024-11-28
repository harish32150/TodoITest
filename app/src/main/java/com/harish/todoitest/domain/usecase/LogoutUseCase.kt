package com.harish.todoitest.domain.usecase

import com.harish.todoitest.domain.KResult

interface LogoutUseCase {
    suspend operator fun invoke(): KResult<Unit>
}