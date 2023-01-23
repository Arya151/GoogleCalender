package com.example.googlecalender

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import androidx.activity.compose.setContent
import com.example.googlecalender.composable.CreateSplashScreen

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        setContent {
            CreateSplashScreen()
            val timer = object: CountDownTimer(3000, 1000) {
                override fun onTick(millisUntilFinished: Long) {}
                override fun onFinish() {
                    MainActivity.startActivity(this@SplashScreen)
                    finish()
                }
            }
            timer.start()
        }
    }
}