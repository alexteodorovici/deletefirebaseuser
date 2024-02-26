package com.alexteodorovici.deletefirebaseuserdemo

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.alexteodorovici.deletefirebaseuserdemo.ui.theme.DeleteFirebaseUserDemoTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class MainActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth
    private val TAG = "MainActivity"
    // Shared MutableState for the current user
    private val currentUserState: MutableState<FirebaseUser?> = mutableStateOf(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Initialize the current user state
        currentUserState.value = auth.currentUser

        setContent {
            DeleteFirebaseUserDemoTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    UserInterface(currentUserState)
                }
            }
        }
    }

    @Composable
    fun UserInterface(userState: MutableState<FirebaseUser?>) {
        val user by remember { userState }
        val context = LocalContext.current

        Column {
            if (user != null) {
                Text(text = "Hello ${user!!.email}!")
            } else {
                Text(text = "No user logged in.")
            }

            Button(onClick = { createAccount("test3@test.com", "123456") }) {
                Text(text = "Create user test3@test.com with password 123456")
            }

            Divider(thickness = 10.dp)

            Button(onClick = { signIn("test3@test.com", "123456") }) {
                Text(text = "Sign In user test3@test.com with password 123456")
            }

            Divider(thickness = 10.dp)

            Button(onClick = { deleteUser(user) }) {
                Text(text = "delete user test3@test.com with password 123456")
            }
        }
    }

    private fun createAccount(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "createUserWithEmail:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }
            }
    }

    private fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithEmail:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }
            }
    }

    private fun deleteUser(user: FirebaseUser?) {
        user?.delete()?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d(TAG, "User account deleted.")
                updateUI(null)
            }
        }
    }

    private fun updateUI(user: FirebaseUser?) {
        // Implement UI update logic here based on user authentication state
        if (user != null) {
            Log.d(TAG, "User is logged in: ${user.email}")
        } else {
            Log.d(TAG, "No user is logged in")
        }
        currentUserState.value = user // Update the shared MutableState
    }

}

