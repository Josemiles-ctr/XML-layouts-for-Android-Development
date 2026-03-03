package com.example.mobileprogrammingxml

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

/**
 * Splash / welcome screen shown on app launch.
 * Displays the app title and a "Get Started" button that navigates to the calculator.
 */
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Lambda capturing the navigation action — reusable if wired to multiple buttons later
        val goToSecond = {
            startActivity(Intent(this, SecondActivity::class.java))
        }

        findViewById<Button>(R.id.start1Button).setOnClickListener { goToSecond() }
    }
}