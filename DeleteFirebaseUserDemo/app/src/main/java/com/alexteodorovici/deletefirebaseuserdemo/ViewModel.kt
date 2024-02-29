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

    private val authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
        currentUserState.value = firebaseAuth.currentUser
    }

    init {
        firebaseAuth.addAuthStateListener(authStateListener)
    }

    override fun onCleared() {
        super.onCleared()
        firebaseAuth.removeAuthStateListener(authStateListener)
    }

    fun logoutUser() {
        viewModelScope.launch {
            try {
                // Sign out the current user
                firebaseAuth.signOut()
                // After logging out, update the current user state to null
//                currentUserState.value = null
            } catch (e: Exception) {
                // Handle any exceptions
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

    fun deleteUser() {
        viewModelScope.launch {
            try {
                currentUserState.value?.delete()?.await()
                // After deletion, clear the current user state
                currentUserState.value = null
                // Optionally, you might want to handle post-deletion logic here, like navigation
            } catch (e: Exception) {
                // Handle any errors, such as network issues or permission errors
                e.printStackTrace()
            }
        }
    }

}