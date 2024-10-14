package com.prafull.secondshelf.di

import com.prafull.secondshelf.network.ApiService
import com.prafull.secondshelf.network.AuthenticatedApiService
import com.prafull.secondshelf.onBoard.login.LoginViewModel
import com.prafull.secondshelf.onBoard.register.RegisterViewModel
import com.prafull.secondshelf.utils.SharedPrefManager
import okhttp3.Credentials
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {
    single<SharedPrefManager> {
        SharedPrefManager(androidContext())
    }
    single<OkHttpClient> {
        OkHttpClient.Builder()
            .addInterceptor { chain ->
                val credentials =
                    Credentials.basic(
                        get<SharedPrefManager>().getUsername() ?: "",
                        get<SharedPrefManager>().getPassword() ?: ""
                    )
                val request = chain.request().newBuilder()
                    .header("Authorization", credentials)
                    .build()
                chain.proceed(request)
            }
            .build()
    }
    single<AuthenticatedApiService> {
        Retrofit.Builder()
            .client(get<OkHttpClient>())
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(AuthenticatedApiService::class.java)
    }
    single<ApiService> {
        Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
    viewModel {
        LoginViewModel()
    }
    viewModel {
        RegisterViewModel()
    }
}

const val BASE_URL = "http://192.168.222.173:8080/"