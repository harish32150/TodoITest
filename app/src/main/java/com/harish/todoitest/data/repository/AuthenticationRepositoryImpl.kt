package com.harish.todoitest.data.repository

import com.harish.todoitest.data.AppPrefs
import com.harish.todoitest.data.datasource.remote.LoginApiRequest
import com.harish.todoitest.data.datasource.remote.RegisterApiRequest
import com.harish.todoitest.data.datasource.remote.RestApi
import com.harish.todoitest.domain.KResult
import com.harish.todoitest.domain.map
import com.harish.todoitest.domain.onSuccess
import com.harish.todoitest.domain.repository.AuthenticationRepository
import javax.inject.Inject

internal class AuthenticationRepositoryImpl @Inject constructor(
    private val api: RestApi,
    private val prefs: AppPrefs
): AuthenticationRepository {
    override suspend fun login(username: String, password: String): KResult<String> =
        LoginApiRequest(username, password)
            .let { api.login(it) }
            .map { it.accessToken }
            .onSuccess {
                prefs.accessToken = it
                prefs.username = username
            }


    override suspend fun register(username: String, password: String): KResult<Long> =
        RegisterApiRequest(username, password)
            .let { api.register(it) }
            .map { it.userId }

    override suspend fun isAuthenticated(): Boolean = !prefs.accessToken.isNullOrEmpty()
}