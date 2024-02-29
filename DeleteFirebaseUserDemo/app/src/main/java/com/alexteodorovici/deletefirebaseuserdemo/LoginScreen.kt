package com.alexteodorovici.deletefirebaseuserdemo

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController

@Composable
fun LoginScreen(navController: NavHostController) {
    val viewModel: ViewModel = viewModel()

    var userEmail: String by remember { mutableStateOf("") }
    var userPassword by remember { mutableStateOf("") }
    val user by remember { viewModel.currentUserState }
    val errorMessage by remember { viewModel.errorMessage }

    val isEmailValid = userEmail.contains('@') && userEmail.contains('.')
    val isPasswordValid = userPassword.length >= 6

    LaunchedEffect(user) {
        if (user != null) {
            navController.navigate(Screen.ProfileScreen.route) {
                popUpTo(Screen.LoginScreen.route) { inclusive = true }
            }
        }
    }

    Column {
        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(20.dp)
            )
        }

        Text(
            modifier = Modifier.padding(20.dp),
            text = "Login user"
        )

        // Email input
        TextField(
            value = userEmail,
            onValueChange = { userEmail = it },
            label = { Text("Email") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            isError = !isEmailValid && userEmail.isNotEmpty()
            // Show error state if email is not valid and not empty
        )

        // Password input
        TextField(
            value = userPassword,
            onValueChange = { userPassword = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            isError = !isPasswordValid && userPassword.isNotEmpty()
            // Show error state if password is not valid and not empty
        )

        Button(
            modifier = Modifier.padding(20.dp),
            onClick = {
                viewModel.loginUser(userEmail, userPassword)
            },
            enabled = isEmailValid && isPasswordValid
            // Enable button only if email and password are valid
        ) {
            Text(text = "Login user")
        }

        Button(
            modifier = Modifier.padding(20.dp),
            onClick = {
                navController.navigate(Screen.RegisterScreen.route)
            }) {
            Text(text = "Register new user")
        }
    }
}