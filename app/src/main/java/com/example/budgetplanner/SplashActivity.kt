package com.example.budgetplanner

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Duration for the splash screen (e.g., 2 seconds)
        val splashScreenDuration = 2000L

        Handler(Looper.getMainLooper()).postDelayed({
            // Start the main activity after the splash screen duration
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish() // Close the SplashActivity
        }, splashScreenDuration)
    }
}
