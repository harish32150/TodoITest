package com.harish.todoitest.data

import android.content.Context
import android.os.Build
import androidx.hilt.work.HiltWorker
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.harish.todoitest.domain.KResult
import com.harish.todoitest.domain.usecase.SyncAllTaskUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.Duration
import javax.inject.Inject


@HiltWorker
class SyncWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    val syncAllTaskUseCase: SyncAllTaskUseCase
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

class SyncWorkerManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val workerFactory: HiltWorkerFactory
) {

    fun enqueueSync() = OneTimeWorkRequestBuilder<SyncWorker>()
        .run {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                setBackoffCriteria(BackoffPolicy.LINEAR, Duration.ofMinutes(1))
            else this
        }
        .setConstraints(
            Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
        )
        .setExpedited(OutOfQuotaPolicy.DROP_WORK_REQUEST)
        .build()
        .let {
            WorkManager.getInstance(context)
                .enqueueUniqueWork(SyncWorkId, ExistingWorkPolicy.REPLACE, it)
        }

    fun cancelSync() = WorkManager.getInstance(context).cancelUniqueWork(SyncWorkId)

    companion object {
        private const val SyncWorkId = "sync_worker"
    }
}