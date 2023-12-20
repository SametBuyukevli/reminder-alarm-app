package com.example.mobilfinaldeneme

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val gorevEkleButton = findViewById<Button>(R.id.extended_fab)

        gorevEkleButton.setOnClickListener() {
            val intent = Intent(this, gorevEkle::class.java)
            startActivity(intent)
        }

    }
}