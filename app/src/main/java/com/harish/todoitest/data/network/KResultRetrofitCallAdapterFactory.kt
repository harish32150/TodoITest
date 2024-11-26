package com.harish.todoitest.data.network

import com.harish.todoitest.domain.KResult
import com.squareup.moshi.Moshi
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import javax.inject.Inject

internal class KResultRetrofitCallAdapterFactory @Inject constructor(
    private val moshi: Moshi
) : CallAdapter.Factory() {
    override fun get(
        returnType: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit,
    ): CallAdapter<*, *>? {
        if (getRawType(returnType) != Call::class.java)
            return null
        if (returnType !is ParameterizedType)
            throw IllegalStateException("$returnType must be parameterized.")

        val responseType = getParameterUpperBound(0, returnType)
        if (getRawType(responseType) != KResult::class.java)
            return null
        if (responseType !is ParameterizedType)
            throw IllegalStateException("$responseType must be parameterized.")

        val successType = getParameterUpperBound(0, responseType)
        return KResultRetrofitCallAdapter<Any>(successType, moshi)

    }
}