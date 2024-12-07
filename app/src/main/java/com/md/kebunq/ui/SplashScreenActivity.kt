package com.md.kebunq.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.md.kebunq.R
import com.md.kebunq.ui.welcome.WelcomeActivity

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        Handler(Looper.getMainLooper()).postDelayed({
            val intent =  Intent(this, WelcomeActivity::class.java)
            startActivity(intent)
            finish()
        }, 15000)
    }
}