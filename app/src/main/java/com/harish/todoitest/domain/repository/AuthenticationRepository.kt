package com.harish.todoitest.domain.repository

import com.harish.todoitest.domain.KResult

interface AuthenticationRepository {
    suspend fun login(username: String, password: String): KResult<String>

    suspend fun register(username: String, password: String): KResult<Long>

    suspend fun isAuthenticated(): Boolean
}