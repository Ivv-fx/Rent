package com.example

import android.app.Application
import com.clerk.api.Clerk

class UrbanRoomApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        
        // Initialize Clerk SDK
        // Replace this with your actual Publishable Key from the Clerk Dashboard
        val publishableKey = "pk_test_cmVsaWV2ZWQtbWlkZ2UtNTMwNS5jbGVyay5hY2NvdW50cy5kZXYk"
        Clerk.initialize(
            context = this,
            publishableKey = publishableKey
        )
    }
}
