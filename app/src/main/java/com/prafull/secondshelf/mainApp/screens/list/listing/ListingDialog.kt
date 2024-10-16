package com.prafull.secondshelf.mainApp.screens.list.listing

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.prafull.secondshelf.model.Transaction
import kotlinx.coroutines.launch

@Composable
fun ListingScreenDialog(
    viewModel: ListingViewModel, bookId: Long, toggleDialog: () -> Unit
) {
    var buyerUsername by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var isSuccess by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    val reset = {
        buyerUsername = ""
        amount = ""
        isLoading = false
        isSuccess = false
    }
    AlertDialog(onDismissRequest = {
        if (!isLoading) {
            toggleDialog()
            reset()
        }
    }, title = {
        Text("Book Sold", style = MaterialTheme.typography.headlineSmall)
    }, text = {
        Column {
            Text("Enter transaction details:", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = buyerUsername,
                onValueChange = { buyerUsername = it },
                label = { Text("Buyer's Username") },
                enabled = !isLoading,
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text
                ),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = amount,
                onValueChange = { amount = it },
                label = { Text("Amount") },
                enabled = !isLoading,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            if (isLoading) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                )
            }
            if (isSuccess) {
                Text(
                    "Book marked as sold successfully!",
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }, confirmButton = {
        Button(
            onClick = {
                if (buyerUsername.isNotBlank() && amount.isNotBlank()) {
                    scope.launch {
                        isLoading = true
                        val transaction = Transaction(
                            transactionId = System.currentTimeMillis(), // Generate a unique ID
                            bookId = bookId,
                            amount = amount.toDoubleOrNull() ?: 0.0,
                            transactionDate = System.currentTimeMillis(),
                            buyerUserName = buyerUsername.lowercase()
                        )
                        scope.launch {
                            isSuccess = viewModel.markBookAsSold(transaction)
                            isLoading = false
                            if (isSuccess) {
                                toggleDialog()
                                reset()
                            }
                        }

                    }
                }
            }, enabled = !isLoading && buyerUsername.isNotBlank() && amount.isNotBlank()
        ) {
            if (isLoading) {
                CircularProgressIndicator()
            } else {
                Text("Update")
            }
        }
    }, dismissButton = {
        TextButton(
            onClick = {
                toggleDialog()
                reset()
            }, enabled = !isLoading
        ) {
            Text("Cancel")
        }
    })

}