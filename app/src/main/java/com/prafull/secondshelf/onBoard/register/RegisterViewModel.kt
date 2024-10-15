package com.prafull.secondshelf.onBoard.register

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prafull.secondshelf.model.User
import com.prafull.secondshelf.network.ApiService
import com.prafull.secondshelf.utils.SharedPrefManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import retrofit2.HttpException
import java.io.IOException


class RegisterViewModel : ViewModel(), KoinComponent {
    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()
    private val apiService by inject<ApiService>()
    private val pref by inject<SharedPrefManager>()
    private val _navigate = MutableStateFlow(false)
    val navigate = _navigate.asStateFlow()
    fun updateUsername(username: String) {
        _uiState.value = _uiState.value.copy(username = username)
    }

    fun updatePassword(password: String) {
        _uiState.value = _uiState.value.copy(password = password)
    }

    fun updateFullName(fullName: String) {
        _uiState.value = _uiState.value.copy(fullName = fullName)
    }

    fun updateMobileNumber(mobileNumber: String) {
        _uiState.value = _uiState.value.copy(mobileNumber = mobileNumber)
    }

    fun register() {
        _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
        viewModelScope.launch {
            try {
                val response = apiService.registerUser(
                    User(
                        username = _uiState.value.username,
                        password = _uiState.value.password,
                        fullName = _uiState.value.fullName,
                        mobileNumber = _uiState.value.mobileNumber
                    )
                )
                if (response.success) {
                    pref.saveUser(_uiState.value.username, _uiState.value.password)
                    _navigate.update { true }
                    _uiState.value = _uiState.value.copy(isLoading = false)
                } else {
                    throw Exception(response.message)
                }
            } catch (e: HttpException) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false, errorMessage = "Http Error"
                )
            } catch (e: IOException) {
                Log.d("RegisterViewModel", "register: ${e.message}")
                _uiState.value = _uiState.value.copy(
                    isLoading = false, errorMessage = "Network Error."
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false, errorMessage = "Registration failed. Please try again."
                )
            }
        }
    }
}

data class RegisterUiState(
    val username: String = "",
    val password: String = "",
    val fullName: String = "",
    val mobileNumber: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)