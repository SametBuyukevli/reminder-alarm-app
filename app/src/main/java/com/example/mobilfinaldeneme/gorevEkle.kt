package com.example.mobilfinaldeneme

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import com.example.mobilfinaldeneme.databinding.ActivityGorevEkleBinding
import java.util.Calendar

class gorevEkle : AppCompatActivity() {
    private lateinit var binding: ActivityGorevEkleBinding
    private lateinit var db :AlarmDatabaseHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGorevEkleBinding.inflate(layoutInflater)
        setContentView(binding.root)


        db = AlarmDatabaseHelper(this)



        val explantionText = findViewById<EditText>(R.id.Aciklama)


        //alarm seçmek için spinner
        val spinner1: Spinner = findViewById(R.id.spinnerAlarmSound)
        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.alarm_sounds,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner1.adapter = adapter
        }

        spinner1.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedSound = parent?.getItemAtPosition(position).toString()

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Hiçbir şey seçilmediğinde yapılacak işlemler
            }
        }

        //hatırlatıcı seçmek için spinner
        val spinner2: Spinner = findViewById(R.id.hatirlaticiSec)
        ArrayAdapter.createFromResource(
            this,
            R.array.hatirlatmadakika,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner2.adapter = adapter
        }




        val saveButton = findViewById<Button>(R.id.saveButton)
        saveButton.setOnClickListener{
            val day = binding.datePicker.dayOfMonth
            val month = binding.datePicker.month
            Log.d("eklenenAy", "Eklenen ay ekleme kisminda: $month") // Log mesajı olarak hangi ayın eklendiğini gösterir
            val year = binding.datePicker.year
            val hour = binding.timePicker.hour
            val minute = binding.timePicker.minute
            Log.d("eklenenSaat","eklenen saat: ${hour} :${minute}")
            val sound = spinner1.selectedItem.toString()
            val explanation = explantionText.text.toString()
            val reminder = spinner2.selectedItem.toString()
            val alarm = AlarmDb(0,day,month,year,hour,minute,sound,explanation,reminder)
            db.insertAlarm(alarm)
            finish()
            Toast.makeText(this,"Alarm Added",Toast.LENGTH_SHORT).show()

        }
    }
}