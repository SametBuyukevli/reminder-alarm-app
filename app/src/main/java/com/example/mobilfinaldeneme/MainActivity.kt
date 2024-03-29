package com.example.mobilfinaldeneme

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mobilfinaldeneme.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var db:AlarmDatabaseHelper
    private lateinit var alarmAdapter: AlarmAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = AlarmDatabaseHelper(this)

        alarmAdapter = AlarmAdapter(db.getAllAlarms(),this)
        // olusturulan alarmlarin anasayfada recyclerview icerisinde gozukmesi
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = alarmAdapter

        //Hatirlatici ekleme sayfasina gecmek icin bir button
        binding.extendedFab.setOnClickListener {
            val intent = Intent(this, gorevEkle::class.java)
            startActivity(intent)
        }
        //Hatirlatici silme sayfasina gecmek icin bir button
        binding.editButton.setOnClickListener(){
            val intent = Intent(this, gorevSilMain::class.java)
            startActivity(intent)
        }

    }
    // Aktivite yeniden başlatıldığında
    override fun onResume() {
        super.onResume()
        val updatedAlarms = db.getAllAlarms()
        Log.d("MainActivity", "Number of alarms: ${updatedAlarms.size}")
        alarmAdapter.refreshData(updatedAlarms)

    }
}