package com.harish.todoitest.data.network

import com.harish.todoitest.data.AppPrefs
import okhttp3.Interceptor
import okhttp3.Response

internal class AuthTokenInterceptor(private val prefs: AppPrefs): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response =
        /* ignore aws urls for uploading files */
        prefs.accessToken?.takeIf { it.isNotEmpty() }?.let {
            chain.request().newBuilder()
                .header("Authorization", "Bearer $it")
                .build()
                .run(chain::proceed)
        } ?: chain.proceed(chain.request())
}