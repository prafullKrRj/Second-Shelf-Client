package com.prafull.secondshelf.mainApp.screens.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import org.koin.core.component.KoinComponent

class HomeViewModel : ViewModel(), KoinComponent {


    var test by mutableStateOf("Hello")
}