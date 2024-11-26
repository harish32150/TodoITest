package com.harish.todoitest.data.network

import com.harish.todoitest.domain.KResult
import com.squareup.moshi.Moshi
import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import java.lang.reflect.Type

internal class KResultRetrofitCallAdapter<T>(
    private val responseType: Type,
    private val moshi: Moshi
) : CallAdapter<T, Call<KResult<T>>> {
    override fun responseType(): Type = responseType

    override fun adapt(call: Call<T>): Call<KResult<T>> = KResultCall<T>(call, ::parseErrorBody)

    private fun parseErrorBody(httpException: HttpException): KResult.Error = try {
        httpException.response()!!.errorBody()?.source()
            ?.run(moshi.adapter(ApiError::class.java).lenient()::fromJson)
            ?.let { KResult.Error(Exception(it.message)) }
            ?: KResult.Error(httpException)
    } catch (e: Exception) {
        KResult.Error(e)
    }
}

private class KResultCall<R>(
    private val delegate: Call<R>,
    private val errorParser: (HttpException) -> KResult.Error
    ): Call<KResult<R>> {

    override fun enqueue(callback: Callback<KResult<R>>) {
        delegate.enqueue(object : Callback<R> {
            override fun onResponse(call: Call<R>, response: Response<R>) {
                when {
                    response.isSuccessful -> KResult.Success(response.body()!!)
                    /*response.code() in 400..511 -> KResult.Error(HttpException(response))*/
                    else -> errorParser(HttpException(response))
                }.let { callback.onResponse(this@KResultCall, Response.success(it)) }
            }

            override fun onFailure(call: Call<R>, throwable: Throwable) {
                throwable.printStackTrace()
                when (throwable) {
                    is HttpException -> errorParser(throwable)
                    else -> KResult.Error(throwable)
                }.let { callback.onResponse(this@KResultCall, Response.success(it)) }
            }
        })
    }

    override fun clone(): Call<KResult<R>> = KResultCall(delegate.clone(), errorParser)

    override fun execute(): Response<KResult<R>> =
        throw UnsupportedOperationException("Synchronous operation is not supported in KResultCall")

    override fun isExecuted(): Boolean = delegate.isExecuted

    override fun cancel() = delegate.cancel()

    override fun isCanceled(): Boolean = delegate.isCanceled

    override fun request(): Request = delegate.request()

    override fun timeout(): Timeout = delegate.timeout()
}