package com.prafull.secondshelf.network

import com.prafull.secondshelf.model.Book
import com.prafull.secondshelf.model.BookResponse
import com.prafull.secondshelf.model.GeneralResponse
import com.prafull.secondshelf.model.Transaction
import com.prafull.secondshelf.model.TransactionResponse
import com.prafull.secondshelf.model.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface AuthenticatedApiService {


    @GET("/api/user/books")
    suspend fun getUserListedBooks(): Response<BookResponse>

    @GET("/api/user")
    suspend fun getUser(): Response<User>

    @PUT("/api/user/update")
    suspend fun updateUserData(
        @Body user: User
    ): Response<User>

    @GET("api/books/search")
    suspend fun searchBooks(@Query("query") query: String): Response<BookResponse>

    @POST("api/books/add")
    suspend fun addBook(@Body bookDto: Book): Response<GeneralResponse>

    @POST("api/books/sell-book")
    suspend fun soldBook(@Body transaction: Transaction): Response<GeneralResponse>


    @GET("/api/books/getRandomBooks")
    suspend fun getHomeScreenBooks(): Response<BookResponse>

    @GET("/api/user/sold-books")
    suspend fun getSoldBooks(): Response<TransactionResponse>

    @GET("/api/user/bought-books")
    suspend fun getBoughtBooks(): Response<TransactionResponse>
}
