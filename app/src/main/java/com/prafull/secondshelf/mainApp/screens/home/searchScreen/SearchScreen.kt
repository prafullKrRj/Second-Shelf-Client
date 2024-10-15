package com.prafull.secondshelf.mainApp.screens.home.searchScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.prafull.secondshelf.goBackStack
import com.prafull.secondshelf.mainApp.screens.home.BooksScreen
import com.prafull.secondshelf.mainApp.screens.home.SearchBar
import com.prafull.secondshelf.mainApp.screens.home.commons.EmptySearchResultsUI
import com.prafull.secondshelf.utils.BC

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(viewModel: SearchViewModel, navController: NavController) {
    val results by viewModel.results.collectAsState()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Searches") },
                navigationIcon = {
                    IconButton(onClick = navController::goBackStack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            when (val state = results) {
                is BC.Loading -> {
                    LinearProgressIndicator()
                }

                is BC.Success -> {
                    if (state.data.isEmpty()) {
                        SearchBar(searchQuery = viewModel.prompt, onValueChange = {
                            viewModel.prompt = it
                            viewModel.getResults()
                        }, onSearch = {
                            viewModel.getResults()
                        })
                        Spacer(modifier = Modifier.padding(8.dp))
                        EmptySearchResultsUI(searchQuery = viewModel.prompt)
                    } else {
                        BooksScreen(navController = navController, books = state.data, onSearch = {
                            viewModel.prompt = it
                            viewModel.getResults()
                        })
                    }
                }

                is BC.Error -> {
                    Text(state.exception.toString())
                }
            }
        }
    }
}