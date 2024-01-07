package com.example.mobilfinaldeneme

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mobilfinaldeneme.databinding.ActivityGorevSilMainBinding


class gorevSilMain : AppCompatActivity(){

    private lateinit var binding: ActivityGorevSilMainBinding
    private lateinit var db :AlarmDatabaseHelper
    private lateinit var alarmAdapter: AlarmDeleteAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGorevSilMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = AlarmDatabaseHelper(this)
        alarmAdapter = AlarmDeleteAdapter(db.getAllAlarms(),this)
        binding.recyclerView1.layoutManager = LinearLayoutManager(this)
        binding.recyclerView1.adapter = alarmAdapter


        //anasayfaya yollar
        binding.checkButton.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }


    }
    override fun onResume() {
        super.onResume()
        val updatedAlarms = db.getAllAlarms()
        Log.d("GorevSilMain", "Number of alarms: ${updatedAlarms.size}")
        alarmAdapter.refreshData(updatedAlarms)

    }

}