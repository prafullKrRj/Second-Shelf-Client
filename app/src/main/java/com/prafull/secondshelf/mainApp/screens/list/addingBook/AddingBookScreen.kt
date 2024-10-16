package com.prafull.secondshelf.mainApp.screens.list.addingBook

import android.graphics.BitmapFactory
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.prafull.secondshelf.goBackStack
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookListingScreen(viewModel: AddingBookViewModel, navController: NavController) {

    var isImageValid by remember { mutableStateOf(false) }
    var isVerifying by remember { mutableStateOf(false) }

    val loading by viewModel.loading.collectAsState()
    val navigateBack by viewModel.bookAdded.collectAsState()
    LaunchedEffect(key1 = navigateBack) {
        if (navigateBack) {
            navController.goBackStack()
        }
    }
    val coroutineScope = rememberCoroutineScope()


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Add New Book Listing")
                },
                navigationIcon = {
                    IconButton(onClick = navController::goBackStack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = viewModel.title,
                onValueChange = { viewModel.title = it },
                label = { Text("Title") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = viewModel.author,
                onValueChange = { viewModel.author = it },
                label = { Text("Author") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = viewModel.yearOfPrinting,
                onValueChange = { viewModel.yearOfPrinting = it },
                label = { Text("Year of Printing") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = viewModel.description,
                onValueChange = { viewModel.description = it },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )

            OutlinedTextField(
                value = viewModel.genre,
                onValueChange = { viewModel.genre = it },
                label = { Text("Genre") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(value = viewModel.coverImageUrl, onValueChange = {
                viewModel.coverImageUrl = it
                isImageValid = false  // Reset validation on new input
            }, label = { Text("Cover Image URL") }, modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    coroutineScope.launch {
                        isVerifying = true
                        isImageValid = verifyImageUrl(viewModel.coverImageUrl)
                        isVerifying = false
                    }
                },
                modifier = Modifier.padding(vertical = 8.dp),
                enabled = viewModel.coverImageUrl.isNotEmpty() && !isVerifying
            ) {
                if (isVerifying) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Verify Image")
                }
            }

            if (isImageValid && viewModel.coverImageUrl.isNotEmpty()) {
                AsyncImage(
                    model = viewModel.coverImageUrl,
                    contentDescription = "Book Cover",
                    modifier = Modifier
                        .size(200.dp)
                        .padding(vertical = 8.dp),
                    contentScale = ContentScale.Fit
                )
            }

            OutlinedTextField(
                value = viewModel.numberOfPages,
                onValueChange = { viewModel.numberOfPages = it },
                label = { Text("Number of Pages") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = viewModel.price,
                onValueChange = { viewModel.price = it },
                label = { Text("Price") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    viewModel.submitListing()
                },
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .fillMaxWidth(),
                enabled = !loading && viewModel.buttonEnabled()
            ) {
                if (loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Save Listing")
                }
            }
        }
    }
}

suspend fun verifyImageUrl(urlString: String): Boolean {
    return withContext(Dispatchers.IO) {
        try {
            val url = URL(urlString)
            val connection = url.openConnection() as HttpURLConnection
            connection.connectTimeout = 5000
            connection.readTimeout = 5000
            connection.requestMethod = "GET"

            val contentType = connection.contentType
            if (!contentType.startsWith("image/")) {
                return@withContext false
            }

            val inputStream = connection.inputStream
            val bitmap = BitmapFactory.decodeStream(inputStream)

            inputStream.close()
            connection.disconnect()

            bitmap != null
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}