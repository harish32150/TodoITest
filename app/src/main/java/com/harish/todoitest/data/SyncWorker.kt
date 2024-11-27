package com.harish.todoitest.data

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.harish.todoitest.domain.KResult
import com.harish.todoitest.domain.usecase.SyncAllTaskUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject


@HiltWorker
class SyncWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val syncAllTaskUseCase: SyncAllTaskUseCase
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result =
        when(val result = syncAllTaskUseCase.invoke()) {
            is KResult.Success -> if (result.value) Result.success() else Result.failure()
            else -> {
                if (result is KResult.Error) result.throwable.printStackTrace()

                Result.retry()
            }
        }
}