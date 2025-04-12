package com.example.chatappdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.chatlibrary.ChatLibrary // Import the public API

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val launchButton: Button = findViewById(R.id.buttonLaunchChat)
        launchButton.setOnClickListener {
            // Call the library's public method
            ChatLibrary.start(this)
        }
    }
}