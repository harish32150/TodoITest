package com.harish.todoitest.data.network

import com.harish.todoitest.data.AppPrefs
import com.harish.todoitest.data.datasource.remote.RestApi
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.Date
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideMoshi(): Moshi = Moshi.Builder()
        .add(Date::class.java, Rfc3339DateJsonAdapter())
        .add(KotlinJsonAdapterFactory())
        .build()

    @Provides
    @Singleton
    internal fun provideAuthTokenInterceptor(appPrefs: AppPrefs): AuthTokenInterceptor =
        AuthTokenInterceptor(appPrefs)

    @Provides
    @Singleton
    internal fun provideOkHttpClient(authInterceptor: AuthTokenInterceptor): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .addInterceptor(authInterceptor)
            /* todo - also add refresh token interceptor */
            .connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(1, TimeUnit.MINUTES)
            .writeTimeout(1, TimeUnit.MINUTES)
            .build()

    @Provides
    @Singleton
    internal fun provideRestApi(client: OkHttpClient, moshi: Moshi): RestApi =
        Retrofit.Builder()
            .client(client)
            .addCallAdapterFactory(KResultRetrofitCallAdapterFactory(moshi))
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(RestApi::class.java)
}