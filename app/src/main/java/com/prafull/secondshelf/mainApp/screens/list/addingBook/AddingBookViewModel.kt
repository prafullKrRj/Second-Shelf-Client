package com.prafull.secondshelf.mainApp.screens.list.addingBook

import android.graphics.Bitmap
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
import java.io.ByteArrayOutputStream
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi


class AddingBookViewModel : ViewModel(), KoinComponent {
    var title by mutableStateOf("")
    var author by mutableStateOf("")
    var description by mutableStateOf("")
    var genre by mutableStateOf("")
    var numberOfPages by mutableStateOf("")
    var price by mutableStateOf("")
    var yearOfPrinting by mutableStateOf("")
    var coverImageUrl by mutableStateOf<String?>(null)

    private val pref by inject<SharedPrefManager>()
    fun setImageBitmap(bitmap: Bitmap) {
        viewModelScope.launch {
            val base64String = convertBitmapToBase64(bitmap)
            coverImageUrl = base64String
        }
    }

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()


    private val service by inject<AuthenticatedApiService>()
    private val _bookAdded = MutableStateFlow(false)
    val bookAdded = _bookAdded.asStateFlow()

    @OptIn(ExperimentalEncodingApi::class)
    private fun convertBitmapToBase64(bitmap: Bitmap): String {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        val byteArray = outputStream.toByteArray()
        return Base64.encode(byteArray)
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
                    coverImageUrl = coverImageUrl ?: "",
                    sellerUserName = pref.getUsername(),
                    sellerFullName = pref.getName(),
                    sellerNumber = pref.getPhoneNumber() ?: "Unknown"
                )
                val response = service.addBook(book)
                Log.d("AddingBookViewModel", "submitListing: ${book.toString()}")
                if (response.isSuccessful) {
                    _bookAdded.value = response.isSuccessful
                } else {
                    Log.d("AddingBookViewModel", "submitListing: ${response.errorBody()?.string()}")
                }

            } catch (e: Exception) {
                Log.d("AddingBookViewModel", "submitListing: ${e.message}")
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