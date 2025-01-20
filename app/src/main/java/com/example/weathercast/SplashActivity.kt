package com.example.weathercast

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.HandlerCompat
import androidx.core.os.postDelayed
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash)
        //TO MOVE THE SPLASH SCREEN TOWARDS THE MAIN ACTIVITY
        //use of Handler is now considered outdated for splash screens.
        // A better approach is to use HandlerCompat or Kotlin Coroutines for smoother execution.
        //
        HandlerCompat.createAsync(Looper.getMainLooper()).postDelayed({

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
            //stopping ths screen for  3seconds
        },3000)

        }
    }
