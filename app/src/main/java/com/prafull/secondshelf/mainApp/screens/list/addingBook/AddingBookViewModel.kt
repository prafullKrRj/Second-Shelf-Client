package com.prafull.secondshelf.mainApp.screens.list.addingBook

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prafull.secondshelf.model.Book
import com.prafull.secondshelf.network.AuthenticatedApiService
import com.prafull.secondshelf.utils.SharedPrefManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


class AddingBookViewModel : ViewModel(), KoinComponent {
    var title by mutableStateOf("")
    var author by mutableStateOf("")
    var description by mutableStateOf("")
    var genre by mutableStateOf("")
    var numberOfPages by mutableStateOf("")
    var price by mutableStateOf("")
    var yearOfPrinting by mutableStateOf("")
    var coverImageUrl by mutableStateOf("")

    private val pref by inject<SharedPrefManager>()

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()


    private val service by inject<AuthenticatedApiService>()
    private val _bookAdded = MutableStateFlow(false)
    val bookAdded = _bookAdded.asStateFlow()

    init {
        _bookAdded.value = false
        _loading.value = false
    }

    fun submitListing() {
        _loading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val book = Book(
                    title = title,
                    author = author,
                    description = description,
                    genre = genre,
                    numberOfPages = numberOfPages.toInt(),
                    price = price.toDouble(),
                    yearOfPrinting = yearOfPrinting.toInt(),
                    coverImageUrl = coverImageUrl,
                    sellerUserName = pref.getUsername(),
                    sellerFullName = pref.getName(),
                    sellerNumber = pref.getPhoneNumber() ?: "Unknown"
                )
                val response = service.addBook(book)
                if (response.isSuccessful) {
                    _bookAdded.value = response.isSuccessful
                } else {
                    Log.d("AddingBookViewModel", "submitListing: ${response.errorBody()?.string()}")
                    _bookAdded.value = false
                }

            } catch (e: Exception) {
                Log.d("AddingBookViewModel", "submitListing: ${e.message}")
                _bookAdded.value = false
                e.printStackTrace()
            }
            _loading.value = false
        }
    }

    fun buttonEnabled(): Boolean {
        return title.isNotBlank() &&
                author.isNotBlank() &&
                description.isNotBlank() &&
                genre.isNotBlank() &&
                numberOfPages.isNotBlank() &&
                price.isNotBlank() &&
                yearOfPrinting.isNotBlank() &&
                coverImageUrl != null
    }
}