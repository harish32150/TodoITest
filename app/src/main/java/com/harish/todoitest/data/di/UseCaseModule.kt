package com.harish.todoitest.data.di

import com.harish.todoitest.data.usecase.SyncAllServerTaskUseCaseImpl
import com.harish.todoitest.data.usecase.SyncTaskUseCaseImpl
import com.harish.todoitest.data.usecase.UpdateTaskLabelUseCaseImpl
import com.harish.todoitest.domain.usecase.SyncAllServerTaskUseCase
import com.harish.todoitest.domain.usecase.SyncTaskUseCase
import com.harish.todoitest.domain.usecase.UpdateTaskLabelUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class UseCaseModule {
    @Binds
    @Singleton
    internal abstract fun bindSyncTaskUseCase(impl: SyncTaskUseCaseImpl): SyncTaskUseCase

    @Binds
    @Singleton
    internal abstract fun bindUpdateTaskLabelUseCase(impl: UpdateTaskLabelUseCaseImpl): UpdateTaskLabelUseCase

    @Binds
    @Singleton
    internal abstract fun bindSyncAllServerTaskUseCase(impl: SyncAllServerTaskUseCaseImpl): SyncAllServerTaskUseCase
}