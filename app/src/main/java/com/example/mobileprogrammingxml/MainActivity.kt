package com.example.mobileprogrammingxml

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val goToSecond = {
            startActivity(Intent(this, SecondActivity::class.java))
        }

        findViewById<Button>(R.id.start1Button).setOnClickListener { goToSecond() }
    }
}