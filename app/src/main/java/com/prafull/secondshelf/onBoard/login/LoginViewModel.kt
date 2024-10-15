package com.prafull.secondshelf.onBoard.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prafull.secondshelf.di.BASE_URL
import com.prafull.secondshelf.model.User
import com.prafull.secondshelf.utils.SharedPrefManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.Credentials
import okhttp3.OkHttpClient
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import retrofit2.HttpException
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.GET
import java.io.IOException

interface LoginInterface {
    @GET("/auth/login")
    suspend fun loginCheck(): Response<User>

}

class LoginViewModel : ViewModel(), KoinComponent {

    private val pref by inject<SharedPrefManager>()
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()
    private val _navigateToHome = MutableStateFlow(false)
    val navigateToHome: StateFlow<Boolean> = _navigateToHome.asStateFlow()
    fun updateEmail(email: String) {
        _uiState.value = _uiState.value.copy(username = email)
    }

    fun updatePassword(password: String) {
        _uiState.value = _uiState.value.copy(password = password)
    }

    fun login() {
        _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val service = Retrofit.Builder().baseUrl(BASE_URL)
                    .client(OkHttpClient.Builder().addInterceptor { chain ->
                        val credentials = Credentials.basic(
                            username = _uiState.value.username,
                            password = _uiState.value.password
                        )
                        val request = chain.request().newBuilder()
                            .header("Authorization", credentials).build()
                        chain.proceed(request)
                    }.build()).addConverterFactory(GsonConverterFactory.create()).build()
                    .create<LoginInterface>()
                val response = service.loginCheck()
                if (response.isSuccessful) {
                    pref.loginUser(response.body()!!)
                    _uiState.value = _uiState.value.copy(isLoading = false)
                    _navigateToHome.value = true
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false, errorMessage = response.errorBody()?.string()
                    )
                }
            } catch (e: HttpException) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false, errorMessage = "Wrong credentials"
                )
            } catch (e: IOException) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false, errorMessage = e.message
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false, errorMessage = e.message
                )
            }
        }
    }
}

data class LoginUiState(
    val username: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)