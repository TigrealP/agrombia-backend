package com.carlos.agrombia.data.api

import com.carlos.agrombia.data.models.TokenResponse
import com.carlos.agrombia.data.models.UserRegisterRequest
import com.carlos.agrombia.data.models.RegisterResponse
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface AgrombiaApi {
    @FormUrlEncoded
    @POST("auth/token")
    suspend fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): TokenResponse

    @POST("auth/register")
    suspend fun register(@Body request: UserRegisterRequest): RegisterResponse
}
