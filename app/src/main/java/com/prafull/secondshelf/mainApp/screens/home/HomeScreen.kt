package com.prafull.secondshelf.mainApp.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.prafull.secondshelf.MainAppRoutes
import com.prafull.secondshelf.R
import com.prafull.secondshelf.mainApp.screens.home.commons.BooksList
import com.prafull.secondshelf.mainApp.screens.home.commons.RetryScreen
import com.prafull.secondshelf.model.Book
import com.prafull.secondshelf.utils.BC

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: HomeViewModel, navController: NavController) {
    val books by viewModel.books.collectAsState()
    Scaffold(topBar = {
        TopAppBar(title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.second_shelf_logo_2),
                    contentDescription = "Application Icon",
                    Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Second Shelf", style = MaterialTheme.typography.titleLarge
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
                    BooksScreen(
                        navController = navController,
                        books = state.data,
                        onSearch = { query ->
                            navController.navigate(
                                MainAppRoutes.SearchScreen(
                                    query
                                )
                            )
                        })
                }

                is BC.Error -> {
                    RetryScreen(
                        retry = viewModel::getMainScreenBooks, error = state.exception.toString()
                    )
                }
            }
        }
    }
}


@Composable
fun BooksScreen(
    navController: NavController,
    initialQuery: String = "",
    books: List<Book>,
    onSearch: (String) -> Unit,
) {
    var searchQuery by remember { mutableStateOf(initialQuery) }
    val context = LocalContext.current
    Column(Modifier.fillMaxSize()) {
        SearchBar(searchQuery = searchQuery, onValueChange = {
            searchQuery = it
        }) {
            onSearch(searchQuery)
        }
        BooksList(books, context, navController)
    }
}

@Composable
fun SearchBar(searchQuery: String, onValueChange: (String) -> Unit, onSearch: () -> Unit) {
    OutlinedTextField(
        value = searchQuery,
        onValueChange = onValueChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        placeholder = { Text("Search books...") },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
        shape = RoundedCornerShape(16.dp),
        singleLine = true,
        trailingIcon = {
            if (searchQuery.isNotEmpty()) {
                IconButton(onClick = {
                    onSearch()
                }) {
                    Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Clear")
                }
            }
        },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(onSearch = {
            if (searchQuery.isNotEmpty()) {
                onSearch()
            }
        })
    )
}