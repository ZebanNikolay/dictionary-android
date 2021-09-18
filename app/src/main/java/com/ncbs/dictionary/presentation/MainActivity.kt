package com.ncbs.dictionary.presentation

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ncbs.dictionary.R

class SplashScreenSampleActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val splashScreen = installSplashScreen()

        setContentView(R.layout.main_activity)
    }

    private fun installSplashScreen() {

    }
}