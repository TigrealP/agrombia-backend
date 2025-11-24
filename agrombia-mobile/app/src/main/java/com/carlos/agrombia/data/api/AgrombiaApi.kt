package com.carlos.agrombia.data.api

import com.carlos.agrombia.data.models.*
import retrofit2.http.*

interface AgrombiaApi {
    @FormUrlEncoded
    @POST("auth/token")
    suspend fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): TokenResponse

    @POST("auth/register")
    suspend fun register(@Body request: UserRegisterRequest): RegisterResponse

    @GET("news/")
    suspend fun getNews(): List<NewsItem>

    // --- CULTIVOS ---
    @GET("cultivos/")
    suspend fun getMyCrops(): List<Crop>

    @POST("cultivos/")
    suspend fun createCrop(@Body crop: CropCreateRequest): Crop

    // --- CLIMA ---
    @GET("climate/crop/{id}")
    suspend fun getCropWeather(@Path("id") cropId: Int): WeatherResponse

    // --- TAREAS ---
    @GET("tareas/by-cultivo/{id}")
    suspend fun getTasksByCrop(@Path("id") cropId: Int): List<Task>

    @POST("tareas/")
    suspend fun createTask(@Body task: TaskCreateRequest): Task

    @DELETE("tareas/{id}")
    suspend fun deleteTask(@Path("id") taskId: Int): Any

    // --- ALERTAS ---
    @GET("alerts/")
    suspend fun getAlerts(): List<Alert>

    // --- REPORTES ---
    @GET("reports/")
    suspend fun getReports(): List<Report>

    @POST("reports/")
    suspend fun createReport(@Body report: ReportCreateRequest): Any
}
