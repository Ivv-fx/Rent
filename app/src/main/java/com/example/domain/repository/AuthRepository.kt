package com.example.domain.repository

import com.example.domain.model.AuthResult
import com.google.firebase.auth.FirebaseUser

interface AuthRepository {
    suspend fun login(email: String, password: String): AuthResult<FirebaseUser>
    suspend fun register(firstName: String, lastName: String, email: String, password: String): AuthResult<FirebaseUser>
    suspend fun loginWithGoogle(idToken: String): AuthResult<FirebaseUser>
    suspend fun resetPassword(email: String): AuthResult<Unit>
    suspend fun logout()
    fun getCurrentUser(): FirebaseUser?
}
