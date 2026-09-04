content = """package com.example.data.repository

import com.example.domain.model.AuthResult
import com.example.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.tasks.await

class FirebaseAuthRepositoryImpl : AuthRepository {

    private val auth: FirebaseAuth? = try {
        FirebaseAuth.getInstance()
    } catch (e: Exception) {
        null
    }

    private fun getError() = AuthResult.Error("Firebase is not configured. Please add google-services.json to the app/ directory.")

    override suspend fun login(email: String, password: String): AuthResult<FirebaseUser> {
        val firebaseAuth = auth ?: return getError()
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            val user = result.user
            if (user != null) {
                AuthResult.Success(user)
            } else {
                AuthResult.Error("Login failed: User is null.")
            }
        } catch (e: Exception) {
            AuthResult.Error(e.localizedMessage ?: "An unknown error occurred during login.")
        }
    }

    override suspend fun register(
        firstName: String,
        lastName: String,
        email: String,
        password: String
    ): AuthResult<FirebaseUser> {
        val firebaseAuth = auth ?: return getError()
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val user = result.user
            
            if (user != null) {
                // Update profile with name
                val profileUpdates = UserProfileChangeRequest.Builder()
                    .setDisplayName("$firstName $lastName")
                    .build()
                user.updateProfile(profileUpdates).await()
                AuthResult.Success(user)
            } else {
                AuthResult.Error("Registration failed: User is null.")
            }
        } catch (e: Exception) {
            AuthResult.Error(e.localizedMessage ?: "An unknown error occurred during registration.")
        }
    }

    override suspend fun loginWithGoogle(idToken: String): AuthResult<FirebaseUser> {
        val firebaseAuth = auth ?: return getError()
        return try {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            val result = firebaseAuth.signInWithCredential(credential).await()
            val user = result.user
            if (user != null) {
                AuthResult.Success(user)
            } else {
                AuthResult.Error("Google Sign-In failed.")
            }
        } catch (e: Exception) {
            AuthResult.Error(e.localizedMessage ?: "An unknown error occurred during Google Sign-In.")
        }
    }

    override suspend fun resetPassword(email: String): AuthResult<Unit> {
        val firebaseAuth = auth ?: return AuthResult.Error("Firebase not configured.")
        return try {
            firebaseAuth.sendPasswordResetEmail(email).await()
            AuthResult.Success(Unit)
        } catch (e: Exception) {
            AuthResult.Error(e.localizedMessage ?: "Failed to send password reset email.")
        }
    }

    override suspend fun logout() {
        auth?.signOut()
    }

    override fun getCurrentUser(): FirebaseUser? {
        return auth?.currentUser
    }
}
"""

with open("app/src/main/java/com/example/data/repository/FirebaseAuthRepositoryImpl.kt", "w") as f:
    f.write(content)
