package com.prafull.secondshelf.mainApp.screens.home

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun HomeScreen(viewModel: HomeViewModel) {

    Text(
        text = viewModel.test
    )
}