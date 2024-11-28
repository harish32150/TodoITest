package com.harish.todoitest

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.WorkManager
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class TodoITestApp: Application(), Configuration.Provider {
    @Inject
    lateinit var hiltWorkerFactory: HiltWorkerFactory

    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder()
            .setMinimumLoggingLevel(android.util.Log.DEBUG)
            .setWorkerFactory(hiltWorkerFactory)
            .build()
    }

    override fun onCreate() {
        super.onCreate()
        WorkManager.initialize(this, workManagerConfiguration)
    }
}