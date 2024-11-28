package com.harish.todoitest.data.usecase

import com.harish.todoitest.data.AppPrefs
import com.harish.todoitest.data.datasource.local.TaskDatabase
import com.harish.todoitest.domain.KResult
import com.harish.todoitest.domain.runCatchingResult
import com.harish.todoitest.domain.usecase.LogoutUseCase
import javax.inject.Inject

internal class LogoutUseCaseImpl @Inject constructor(
    private val db: TaskDatabase,
    private val prefs: AppPrefs
): LogoutUseCase {
    override suspend operator fun invoke(): KResult<Unit> = runCatchingResult {
        db.taskDao().deleteAll()
        prefs.apply {
            username = null
            accessToken = null
        }
    }
}