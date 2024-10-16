package com.prafull.secondshelf.mainApp.screens.books

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prafull.secondshelf.model.TransactionResponse
import com.prafull.secondshelf.network.AuthenticatedApiService
import com.prafull.secondshelf.utils.BC
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class BookViewModel : ViewModel(), KoinComponent {

    private val service by inject<AuthenticatedApiService>()
    private val _soldBooks = MutableStateFlow<BC<TransactionResponse>>(BC.Loading)
    private val _boughtBooks = MutableStateFlow<BC<TransactionResponse>>(BC.Loading)
    val soldBooks = _soldBooks.asStateFlow()
    val boughtBooks = _boughtBooks.asStateFlow()

    init {
        viewModelScope.launch {
            if (_soldBooks.value !is BC.Success) {
                getSoldBooks()
            }
            if (_boughtBooks.value !is BC.Success) {
                getBoughtBooks()
            }
        }

    }

    suspend fun getSoldBooks() {
        try {
            _soldBooks.value = BC.Loading
            val response = service.getSoldBooks()
            if (response.isSuccessful) {
                _soldBooks.value = BC.Success(response.body()!!)
            } else {
                _soldBooks.value = BC.Error(Exception(response.message()))
            }
            Log.d("BookViewModel", "getSoldBooks: ${response.body()}")
        } catch (e: Exception) {
            _soldBooks.value = BC.Error(e)
        }
    }

    suspend fun getBoughtBooks() {
        try {
            _boughtBooks.value = BC.Loading
            val response = service.getBoughtBooks()
            if (response.isSuccessful) {
                _boughtBooks.value = BC.Success(response.body()!!)
            } else {
                _boughtBooks.value = BC.Error(Exception(response.message()))
            }
        } catch (e: Exception) {
            _boughtBooks.value = BC.Error(e)
        }
    }

    suspend fun getBooks() {
        getBoughtBooks()
        getSoldBooks()
    }
}