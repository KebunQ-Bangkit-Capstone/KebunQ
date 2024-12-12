package com.md.kebunq.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.md.kebunq.MainActivity
import com.md.kebunq.R
import com.md.kebunq.ui.welcome.WelcomeActivity

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        supportActionBar?.hide()

        val sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE)
        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", true)

        Handler(Looper.getMainLooper()).postDelayed({
            if (isLoggedIn) {
                // Pengguna sudah login, arahkan ke HomeActivity
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
            val intent =  Intent(this, WelcomeActivity::class.java)
            startActivity(intent)
            finish()
        }, 4000)
    }
}