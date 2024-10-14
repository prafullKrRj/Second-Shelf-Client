package com.prafull.secondshelf.network

import com.prafull.secondshelf.onBoard.model.Book
import com.prafull.secondshelf.onBoard.model.GeneralResponse
import com.prafull.secondshelf.onBoard.model.Transaction
import com.prafull.secondshelf.onBoard.model.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface AuthenticatedApiService {


    @GET("/api/user/books")
    suspend fun getUserListedBooks(): Book

    @PUT("/api/user/update")
    suspend fun updateUserData(
        @Body user: User
    ): GeneralResponse

    @GET("api/books/search")
    suspend fun searchBooks(@Query("query") query: String): Response<List<Book>>

    @POST("api/books/add")
    suspend fun addBook(@Body bookDto: Book): Book

    @POST("api/books/sold-book")
    suspend fun soldBook(@Body transaction: Transaction): Response<String>


}