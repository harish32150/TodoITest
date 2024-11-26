package com.harish.todoitest.data.datasource.remote

import com.squareup.moshi.Json

internal data class RegisterApiRequest(
    val username: String,
    val password: String,
)

internal data class RegisterApiResponse(
    val userId: Long
)

internal data class LoginApiRequest(
    val username: String,
    val password: String,
)

internal data class LoginApiResponse(
    @Json(name = "jwt_token") val accessToken: String
)