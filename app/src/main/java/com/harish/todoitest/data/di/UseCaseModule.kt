package com.harish.todoitest.data.di

import com.harish.todoitest.data.usecase.SyncTaskUseCaseImpl
import com.harish.todoitest.domain.usecase.SyncTaskUseCase
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
}