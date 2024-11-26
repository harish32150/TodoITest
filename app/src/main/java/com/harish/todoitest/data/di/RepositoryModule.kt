package com.harish.todoitest.data.di

import com.harish.todoitest.data.repository.AuthenticationRepositoryImpl
import com.harish.todoitest.data.repository.TaskRepositoryImpl
import com.harish.todoitest.domain.repository.AuthenticationRepository
import com.harish.todoitest.domain.repository.TaskRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    internal abstract fun bindAuthRepository(impl: AuthenticationRepositoryImpl): AuthenticationRepository

    @Binds
    @Singleton
    internal abstract fun bindTaskRepository(impl: TaskRepositoryImpl): TaskRepository
}