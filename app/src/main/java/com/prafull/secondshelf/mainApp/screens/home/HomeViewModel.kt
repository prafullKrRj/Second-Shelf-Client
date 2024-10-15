package com.prafull.secondshelf.mainApp.screens.home

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prafull.secondshelf.model.Book
import com.prafull.secondshelf.network.ApiService
import com.prafull.secondshelf.network.AuthenticatedApiService
import com.prafull.secondshelf.utils.BC
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class HomeViewModel : ViewModel(), KoinComponent {

    var test by mutableStateOf("Hello")
    private val apiService by inject<ApiService>()
    private val service by inject<AuthenticatedApiService>()
    private val _books = MutableStateFlow<BC<List<Book>>>(BC.Loading)
    val books get() = _books.asStateFlow()

    init {
        healthCheck()
        if (books.value is BC.Loading) {
            getMainScreenBooks()
        }
    }

    fun getMainScreenBooks() {
        _books.update {
            BC.Loading
        }
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = service.getHomeScreenBooks()
                if (response.isSuccessful) {
                    _books.update {
                        BC.Success(response.body()!!)
                    }
                } else {
                    _books.update {
                        BC.Error(Exception(response.message()))
                    }
                    Log.d("HomeViewModel", "Error: ${response.message()}")
                }
            } catch (e: Exception) {
                _books.update {
                    BC.Error(e)
                }
                Log.d("HomeViewModel", "Error: $e")
            }
        }
    }

    private fun healthCheck() {
        viewModelScope.launch {
            val response = apiService.healthCheck()
            Log.d("HomeViewModel", "Response: $response")
        }
    }
}