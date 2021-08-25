package com.ncbs.dictionary.presentation

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.ncbs.dictionary.R

class SplashScreenActivity : AppCompatActivity() {

    private val splashScreentimeout : Long = 2500


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        Handler().postDelayed({

                startActivity(Intent(this, MainActivity::class.java))
                finish()

        }, splashScreentimeout)
    }
}