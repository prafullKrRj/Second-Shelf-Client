package com.prafull.secondshelf.network

import com.prafull.secondshelf.onBoard.model.GeneralResponse
import com.prafull.secondshelf.onBoard.model.User
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("/auth/register")
    suspend fun registerUser(
        @Body body: User
    ): GeneralResponse
}