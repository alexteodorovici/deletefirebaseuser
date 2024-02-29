package com.alexteodorovici.deletefirebaseuserdemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.alexteodorovici.deletefirebaseuserdemo.ui.theme.DeleteFirebaseUserDemoTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DeleteFirebaseUserDemoTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation()
                }
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.LoginScreen.route) {
        composable(Screen.LoginScreen.route) { LoginScreen(navController) }
        composable(Screen.ProfileScreen.route) { ProfileScreen(navController) }
        composable(Screen.RegisterScreen.route) { RegisterScreen(navController) }
    }
}

sealed class Screen(val route: String) {
    object LoginScreen : Screen("loginScreen")
    object ProfileScreen : Screen("profileScreen")
    object RegisterScreen : Screen("registerScreen")
}