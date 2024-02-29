package com.alexteodorovici.deletefirebaseuserdemo

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ViewModel : ViewModel() {

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    val currentUserState: MutableState<FirebaseUser?> = mutableStateOf(firebaseAuth.currentUser)
    val errorMessage: MutableState<String> = mutableStateOf("")

    fun logoutUser(onLogoutComplete: () -> Unit) {
        viewModelScope.launch {
            try {
                firebaseAuth.signOut()
                onLogoutComplete() // Invoke the callback after successful logout
//                currentUserState.value = null
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    fun loginUser(userEmail: String, userPassword: String) {
        viewModelScope.launch {
            try {
                // Attempt to sign in the user with email and password
                val authResult = firebaseAuth.signInWithEmailAndPassword(userEmail, userPassword).await()
                // On success, update the currentUserState with the logged in user
                currentUserState.value = authResult.user
                errorMessage.value = ""

            } catch (e: Exception) {
                // Handle any login failures, e.g., incorrect credentials, no internet connection
                val message = e.localizedMessage ?: "An error occurred during login."
                errorMessage.value = message
                e.printStackTrace()
                // Optionally, update UI or state to reflect the error
                currentUserState.value = null
            }
        }
    }

    fun registerUser(email: String, password: String) {
        viewModelScope.launch {
            try {
                val authResult = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
                currentUserState.value = authResult.user
                // Clear any previous error message upon successful registration
                errorMessage.value = ""
            } catch (e: Exception) {
                // Extract and set the Firebase Auth error message
                val message = e.localizedMessage ?: "An error occurred during registration."
                errorMessage.value = message
                e.printStackTrace()
            }
        }
    }

    fun deleteUser(onDeleteComplete: () -> Unit) {
        val currentUser = firebaseAuth.currentUser
        currentUser?.let { user ->
            viewModelScope.launch {
                try {
                    user.delete().await()
                    onDeleteComplete() // Invoke the callback after successful deletion
//                    currentUserState.value = null
                } catch (e: Exception) {
                    errorMessage.value = e.localizedMessage ?: "An error occurred during the deletion process."
                    e.printStackTrace()
                }
            }
        }
    }

}