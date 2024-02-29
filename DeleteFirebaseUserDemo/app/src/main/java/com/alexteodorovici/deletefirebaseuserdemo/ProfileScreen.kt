package com.alexteodorovici.deletefirebaseuserdemo

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController

@Composable
fun ProfileScreen(navController: NavHostController) {

    val viewModel: ViewModel = viewModel()
    val user by remember { viewModel.currentUserState }
    val errorMessage by remember { viewModel.errorMessage }

    LaunchedEffect(user) {
        if (user == null) {
            navController.navigate(Screen.LoginScreen.route) {
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
            text = "Hello ${user?.email}!"
        )

        Button(
            modifier = Modifier.padding(20.dp),
            onClick = {
                viewModel.logoutUser()
            }) {
            Text(text = "Logout user ${user?.email}")
        }

        // Delete User Button
        Button(
            modifier = Modifier.padding(20.dp),
            onClick = {
                viewModel.deleteUser()
            }) {
            Text(text = "Delete user ${user?.email}")
        }
    }
}