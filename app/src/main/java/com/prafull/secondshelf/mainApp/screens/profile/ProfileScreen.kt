package com.prafull.secondshelf.mainApp.screens.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.prafull.secondshelf.mainApp.screens.home.commons.RetryScreen
import com.prafull.secondshelf.model.User
import com.prafull.secondshelf.utils.BC

@Composable
fun UserProfileScreen(
    viewModel: ProfileViewModel,
    logout: () -> Unit = {}
) {
    val user by viewModel.userState.collectAsState()
    when (val userState = user) {
        is BC.Loading -> {
            LinearProgressIndicator()
        }

        is BC.Error -> {
            RetryScreen(retry = viewModel::getUser)
        }

        is BC.Success -> {
            ProfileSuccessScreen(userState.data, viewModel, logout)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileSuccessScreen(user: User, viewModel: ProfileViewModel, logout: () -> Unit = {}) {
    var fullName by remember { mutableStateOf(user.fullName ?: "") }
    var mobileNumber by remember { mutableStateOf(user.mobileNumber ?: "") }
    Scaffold(topBar = {
        TopAppBar(title = { Text("User Profile") })
    }) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            ProfileField(
                label = "Username", value = user.username, readOnly = true
            )
            ProfileField(
                label = "Full Name",
                value = fullName,
                onValueChange = { fullName = it },
                readOnly = true
            )

            ProfileField(
                label = "Mobile Number",
                value = mobileNumber,
                onValueChange = { mobileNumber = it },
                readOnly = true
            )


            Spacer(modifier = Modifier.height(24.dp))

            FilledTonalButton(
                onClick = logout, modifier = Modifier.fillMaxWidth()
            ) {
                Text("Logout")
            }
        }
    }
}

@Composable
fun ProfileField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit = {},
    readOnly: Boolean = false,
    isPassword: Boolean = false
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        readOnly = readOnly,
        enabled = !readOnly,
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        colors = OutlinedTextFieldDefaults.colors(
            disabledTextColor = MaterialTheme.colorScheme.onSurface,
            disabledBorderColor = MaterialTheme.colorScheme.outline,
            disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    )
}