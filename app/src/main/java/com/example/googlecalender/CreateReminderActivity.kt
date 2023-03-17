package com.example.googlecalender

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class CreateReminderActivity : AppCompatActivity() {

    companion object {
        fun startActivity(context: Context) {
            val intent = Intent(context,CreateReminderActivity::class.java)

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_reminder)
    }
}