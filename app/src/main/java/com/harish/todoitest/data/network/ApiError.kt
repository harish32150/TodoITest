package com.harish.todoitest.data.network

import com.squareup.moshi.Json

internal data class ApiError(
    @Json(name = "message") val errorMessage: String
): Throwable() {
    override val message: String
        get() = errorMessage
}