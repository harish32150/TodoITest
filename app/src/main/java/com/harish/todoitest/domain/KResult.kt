package com.harish.todoitest.domain


sealed interface KResult<out T> {
    @JvmInline
    value class Success<out T>(val value: T): KResult<T>

    data object Loading: KResult<Nothing>

    @JvmInline
    value class Error(val throwable: Throwable): KResult<Nothing>
}

inline fun <T> runCatchingResult(block: () -> T): KResult<T> =
    try {
        KResult.Success(block())
    } catch (e: Exception) {
        KResult.Error(e)
    }

inline fun <T> KResult<T>.handle(
    onSuccess: (T) -> Unit,
    onLoading: (Boolean) -> Unit = {},
    onError: (Throwable) -> Unit = {}
) {
    when(this) {
        is KResult.Success -> {
            onLoading(false)
            onSuccess(value)
        }
        is KResult.Loading -> onLoading(true)
        is KResult.Error -> {
            onLoading(false)
            onError(throwable)
        }
    }
}

inline fun <T, R> KResult<T>.map(transform: (value: T) -> R): KResult<R> =
    when (this) {
        is KResult.Success -> KResult.Success(transform(value))
        is KResult.Error -> KResult.Error(throwable)
        is KResult.Loading -> KResult.Loading
    }

inline fun <T, R> KResult<T>.flatMap(transform: (value: T) -> KResult<R>): KResult<R> =
    when (this) {
        is KResult.Success -> transform(value)
        is KResult.Error -> KResult.Error(throwable)
        is KResult.Loading -> KResult.Loading
    }

inline fun <T> KResult<T>.onSuccess(block: (T) -> Unit): KResult<T> {
    if (this is KResult.Success) block.invoke(value)
    return this
}

inline fun <T> KResult<T>.onError(block: (Throwable) -> Unit): KResult<T> {
    if (this is KResult.Error) block.invoke(throwable)
    return this
}

fun <T> KResult<T>.get(): T {
    return if (this is KResult.Success) value
    else error("result is not success")
}

fun <T> KResult<T>.getOrNull(): T? {
    return if (this is KResult.Success) value
    else null
}