package com.harish.todoitest.data.repository

import com.harish.todoitest.domain.KResult
import com.harish.todoitest.domain.repository.AuthenticationRepository
import javax.inject.Inject

internal class AuthenticationRepositoryImpl @Inject constructor(): AuthenticationRepository {
    override suspend fun login(username: String, password: String): KResult<String> {
        TODO("Not yet implemented")
    }

    override suspend fun register(username: String, password: String): KResult<Long> {
        TODO("Not yet implemented")
    }
}