package com.prafull.secondshelf.mainApp.screens.home.searchScreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prafull.secondshelf.model.Book
import com.prafull.secondshelf.network.AuthenticatedApiService
import com.prafull.secondshelf.utils.BC
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SearchViewModel(
    private val initialPrompt: String
) : ViewModel(), KoinComponent {

    var prompt by mutableStateOf(initialPrompt)
    private val service by inject<AuthenticatedApiService>()

    private val _results = MutableStateFlow<BC<List<Book>>>(BC.Loading)
    val results = _results.asStateFlow()

    init {
        getResults()
    }

    fun getResults() {
        _results.value = BC.Loading
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = service.searchBooks(prompt)
                if (response.isSuccessful) {
                    _results.value = BC.Success(response.body()!!)
                } else {
                    _results.value = BC.Error(Exception(response.errorBody().toString()))
                }
            } catch (e: Exception) {
                _results.value = BC.Error(e)
            }
        }
    }
}