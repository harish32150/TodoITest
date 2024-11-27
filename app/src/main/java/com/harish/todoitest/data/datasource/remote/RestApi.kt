package com.harish.todoitest.data.datasource.remote

import com.harish.todoitest.domain.KResult
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

internal interface RestApi {
    @POST("/auth/register")
    suspend fun register(@Body payload: RegisterApiRequest): KResult<RegisterApiResponse>

    @POST("/auth/login")
    suspend fun login(@Body payload: LoginApiRequest): KResult<LoginApiResponse>

    @GET("/tasks/")
    suspend fun taskList(): KResult<List<TaskApiModel>>

    @POST("/tasks")
    suspend fun createTask(@Body payload: TaskApiModel): KResult<TaskApiModel>

    @PUT("/tasks/{id}")
    suspend fun updateTask(@Path("id") id: Long, @Body payload: TaskApiModel): KResult<TaskApiModel>

    @DELETE("/tasks/{id}")
    suspend fun deleteTask(@Path("id") id: Long): KResult<Unit>
}