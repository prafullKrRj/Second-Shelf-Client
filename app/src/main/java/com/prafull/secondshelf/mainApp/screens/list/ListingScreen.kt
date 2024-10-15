package com.prafull.secondshelf.mainApp.screens.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.prafull.secondshelf.mainApp.screens.home.commons.BookCard
import com.prafull.secondshelf.mainApp.screens.home.commons.RetryScreen
import com.prafull.secondshelf.utils.BC

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListingScreen(viewModel: ListingViewModel, navController: NavController) {
    val books by viewModel.listedBooks.collectAsState()
    val context = LocalContext.current

    Scaffold(topBar = {
        TopAppBar(title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "📃 Second Shelf", style = MaterialTheme.typography.titleLarge
                )
            }
        })
    }) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (val state = books) {
                is BC.Loading -> {
                    LinearProgressIndicator()
                }

                is BC.Success -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(state.data, key = {
                            it.id
                        }) { book ->
                            BookCard(book, context) {

                            }
                        }
                    }
                }

                is BC.Error -> {
                    RetryScreen(
                        retry = viewModel::getBooks, error = state.exception.toString()
                    )
                }
            }
        }
    }
}