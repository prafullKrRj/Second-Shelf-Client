package com.prafull.secondshelf.mainApp.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import org.koin.core.component.KoinComponent

class MainViewModel : ViewModel(), KoinComponent {

    var viewModels by mutableStateOf<Map<String, ViewModel>>(mutableMapOf())

    fun logout() = Unit
}