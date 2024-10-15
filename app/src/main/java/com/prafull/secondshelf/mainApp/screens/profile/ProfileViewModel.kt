package com.prafull.secondshelf.mainApp.screens.profile

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prafull.secondshelf.model.User
import com.prafull.secondshelf.network.AuthenticatedApiService
import com.prafull.secondshelf.utils.BC
import com.prafull.secondshelf.utils.SharedPrefManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ProfileViewModel : ViewModel(), KoinComponent {

    private val service by inject<AuthenticatedApiService>()
    private val pref by inject<SharedPrefManager>()
    private val _userState = MutableStateFlow<BC<User>>(BC.Loading)

    val userState = _userState.asStateFlow()
    var lastUser by mutableStateOf<User?>(null)

    init {
        if (_userState.value !is BC.Success) {
            getUser()
        }
    }

    fun getUser() {
        viewModelScope.launch {
            try {
                val response = service.getUser()
                if (response.isSuccessful) {
                    _userState.value = BC.Success(response.body()!!)
                } else {
                    _userState.value = BC.Error(Exception(response.message()))
                }
            } catch (e: Exception) {
                Log.d("ProfileViewModel", "getUser: ${e.message}")
                _userState.value = BC.Error(e)
            }
        }
    }

    fun updateUser(lUser: User, newUser: User) {
        viewModelScope.launch {
            try {
                lastUser = lUser
                _userState.value = BC.Loading
                val response = service.updateUserData(newUser)
                if (response.isSuccessful) {
                    pref.loginUser(newUser)
                    _userState.value = BC.Success(newUser)
                } else {
                    _userState.value = BC.Error(Exception(response.message()))
                }
            } catch (e: Exception) {
                _userState.value = BC.Error(e)
            }
        }
    }
}