package com.prafull.secondshelf.network

import com.prafull.secondshelf.model.GeneralResponse
import com.prafull.secondshelf.model.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    @POST("/auth/register")
    suspend fun registerUser(
        @Body body: User
    ): Response<GeneralResponse>

    @GET("/public/health-check")
    suspend fun healthCheck(): Response<GeneralResponse>
}