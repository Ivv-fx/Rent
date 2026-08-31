package com.example.data.api

import com.squareup.moshi.JsonClass
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

@JsonClass(generateAdapter = true)
data class ClerkSignInRequest(
    val identifier: String,
    val strategy: String = "password",
    val password: String
)

@JsonClass(generateAdapter = true)
data class ClerkSignInResponse(
    val response: ClerkResponseData?
)

@JsonClass(generateAdapter = true)
data class ClerkResponseData(
    val id: String?,
    val status: String?
)

interface ClerkApiService  {
    @POST("v1/client/sign_ins")
    suspend fun createSignIn(
        @Query("_clerk_js_version") version: String = "4.73.4",
        @Body request: ClerkSignInRequest
    ): Response<ClerkSignInResponse>
}
