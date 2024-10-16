package com.prafull.secondshelf.mainApp.screens.list.listing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prafull.secondshelf.model.Book
import com.prafull.secondshelf.model.Transaction
import com.prafull.secondshelf.network.AuthenticatedApiService
import com.prafull.secondshelf.utils.BC
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


class ListingViewModel() : ViewModel(), KoinComponent {
    private val service: AuthenticatedApiService by inject()

    private val _listedBooks = MutableStateFlow<BC<List<Book>>>(BC.Loading)
    val listedBooks = _listedBooks.asStateFlow()

    init {
        viewModelScope.launch {
            if (_listedBooks.value !is BC.Success) {
                getBooks()
            }
        }
    }

    suspend fun getBooks() {
        _listedBooks.value = BC.Loading
        try {
            val books = service.getUserListedBooks()
            if (books.isSuccessful) {
                _listedBooks.value = BC.Success(books.body() ?: emptyList())
            } else {
                _listedBooks.value =
                    BC.Error(Exception(books.errorBody()?.string() ?: "Unknown error"))
            }
        } catch (e: Exception) {
            _listedBooks.value = BC.Error(e)
        }
    }

    fun getBooksNonSuspend() {
        viewModelScope.launch {
            getBooks()
        }
    }

    suspend fun markBookAsSold(trans: Transaction): Boolean {
        try {
            val response = service.soldBook(trans)
            return response.isSuccessful && response.body()?.success == true
        } catch (e: Exception) {
            return false
        }
    }
}