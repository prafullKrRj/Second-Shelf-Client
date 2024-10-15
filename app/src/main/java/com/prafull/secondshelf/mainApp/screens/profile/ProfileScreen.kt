package com.prafull.secondshelf.mainApp.screens.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.prafull.secondshelf.model.User

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfileScreen(
    user: User,
    onUpdateUser: (User) -> Unit
) {
    var editMode by remember { mutableStateOf(false) }
    var fullName by remember { mutableStateOf(user.fullName ?: "") }
    var mobileNumber by remember { mutableStateOf(user.mobileNumber ?: "") }
    var password by remember { mutableStateOf(user.password) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("User Profile") },
                actions = {
                    IconButton(onClick = { editMode = !editMode }) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit Profile")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            ProfileField(
                label = "Username",
                value = user.username,
                readOnly = true
            )

            ProfileField(
                label = "Full Name",
                value = fullName,
                onValueChange = { fullName = it },
                readOnly = !editMode
            )

            ProfileField(
                label = "Mobile Number",
                value = mobileNumber,
                onValueChange = { mobileNumber = it },
                readOnly = !editMode
            )

            ProfileField(
                label = "Password",
                value = password,
                onValueChange = { password = it },
                readOnly = !editMode,
                isPassword = true
            )

            Spacer(modifier = Modifier.height(24.dp))

            if (editMode) {
                Button(
                    onClick = {
                        onUpdateUser(
                            user.copy(
                                fullName = fullName,
                                mobileNumber = mobileNumber,
                                password = password
                            )
                        )
                        editMode = false
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Save Changes")
                }
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