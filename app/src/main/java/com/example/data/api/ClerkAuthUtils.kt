package com.example.data.api

import android.util.Base64
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object ClerkAuthUtils  {
    fun getFrontendApiUrl(publishableKey: String): String  {
        return try  {
            if (publishableKey.startsWith("pk_test_") || publishableKey.startsWith("pk_live_"))  {
                val base64Part = publishableKey.substringAfter("_").substringAfter("_").trimEnd('$')
                val decodedBytes = Base64.decode(base64Part, Base64.NO_PADDING or Base64.NO_WRAP or Base64.URL_SAFE)
                val decodedString = String(decodedBytes, Charsets.UTF_8).trimEnd('$')
                "https://${decodedString}/"
            } else  {
                "https://clerk.dev/"
            }
        } catch (e: Exception)  {
            "https://clerk.dev/"
        }
    }

    fun createClerkApi(publishableKey: String): ClerkApiService  {
        val baseUrl = getFrontendApiUrl(publishableKey)
        
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
            
        val client = OkHttpClient.Builder().build()

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(ClerkApiService::class.java)
    }
}
