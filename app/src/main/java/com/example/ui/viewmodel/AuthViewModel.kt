package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.local.SessionManager
import com.example.domain.model.AuthResult
import com.example.domain.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class AuthUiState {
    object Idle : AuthUiState()
    object Loading : AuthUiState()
    data class Success(val message: String = "") : AuthUiState()
    data class Error(val message: String) : AuthUiState()
}

class AuthViewModel(
    private val repository: AuthRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()
    
    val isLoggedIn = sessionManager.userIdFlow

    fun login(email: String, password: String) {
        if (!isValidEmail(email)) {
            _uiState.value = AuthUiState.Error("Invalid email format.")
            return
        }
        if (password.isBlank()) {
            _uiState.value = AuthUiState.Error("Password cannot be empty.")
            return
        }

        _uiState.value = AuthUiState.Loading
        viewModelScope.launch {
            when (val result = repository.login(email, password)) {
                is AuthResult.Success -> {
                    val user = result.data
                    sessionManager.saveSession(user.uid, user.email ?: "", user.displayName ?: "User")
                    _uiState.value = AuthUiState.Success("Logged in successfully.")
                }
                is AuthResult.Error -> {
                    _uiState.value = AuthUiState.Error(result.message)
                }
                else -> Unit
            }
        }
    }

    fun register(firstName: String, lastName: String, email: String, password: String, confirmPassword: String) {
        if (!isValidEmail(email)) {
            _uiState.value = AuthUiState.Error("Invalid email format.")
            return
        }
        if (!isSecurePassword(password)) {
            _uiState.value = AuthUiState.Error("Password must be at least 8 characters, contain 1 number and 1 special character.")
            return
        }
        if (password != confirmPassword) {
            _uiState.value = AuthUiState.Error("Passwords do not match.")
            return
        }

        _uiState.value = AuthUiState.Loading
        viewModelScope.launch {
            when (val result = repository.register(firstName, lastName, email, password)) {
                is AuthResult.Success -> {
                    val user = result.data
                    sessionManager.saveSession(user.uid, user.email ?: "", user.displayName ?: "$firstName $lastName")
                    _uiState.value = AuthUiState.Success("Registered successfully.")
                }
                is AuthResult.Error -> {
                    _uiState.value = AuthUiState.Error(result.message)
                }
                else -> Unit
            }
        }
    }

    fun loginWithGoogle(idToken: String) {
        _uiState.value = AuthUiState.Loading
        viewModelScope.launch {
            when (val result = repository.loginWithGoogle(idToken)) {
                is AuthResult.Success -> {
                    val user = result.data
                    sessionManager.saveSession(user.uid, user.email ?: "", user.displayName ?: "Google User")
                    _uiState.value = AuthUiState.Success("Logged in with Google.")
                }
                is AuthResult.Error -> {
                    _uiState.value = AuthUiState.Error(result.message)
                }
                else -> Unit
            }
        }
    }

    fun resetPassword(email: String) {
        if (!isValidEmail(email)) {
            _uiState.value = AuthUiState.Error("Invalid email format.")
            return
        }
        
        _uiState.value = AuthUiState.Loading
        viewModelScope.launch {
            when (val result = repository.resetPassword(email)) {
                is AuthResult.Success -> {
                    _uiState.value = AuthUiState.Success("Password reset email sent.")
                }
                is AuthResult.Error -> {
                    _uiState.value = AuthUiState.Error(result.message)
                }
                else -> Unit
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
            sessionManager.clearSession()
            _uiState.value = AuthUiState.Idle
        }
    }

    fun continueAsGuest() {
        viewModelScope.launch {
            sessionManager.saveSession("guest_user", "guest@urbanroom.app", "Guest Explorer")
            _uiState.value = AuthUiState.Success("Welcome, Guest!")
        }
    }

    fun setErrorMessage(message: String) {
        _uiState.value = AuthUiState.Error(message)
    }

    fun clearState() {
        _uiState.value = AuthUiState.Idle
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isSecurePassword(password: String): Boolean {
        val hasMinLength = password.length >= 8
        val hasNumber = password.any { it.isDigit() }
        val hasSpecialChar = password.any { !it.isLetterOrDigit() }
        return hasMinLength && hasNumber && hasSpecialChar
    }
}

class AuthViewModelFactory(
    private val repository: AuthRepository,
    private val sessionManager: SessionManager
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            return AuthViewModel(repository, sessionManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
