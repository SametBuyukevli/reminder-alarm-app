package com.example.mobilfinaldeneme

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import com.example.mobilfinaldeneme.databinding.ActivityGorevEkleBinding

class gorevEkle : AppCompatActivity() {
    private lateinit var binding: ActivityGorevEkleBinding
    private lateinit var db :AlarmDatabaseHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGorevEkleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = AlarmDatabaseHelper(this)



        val explantionText = findViewById<EditText>(R.id.Aciklama)
        binding.numPickerHour.minValue = 0
        binding.numPickerHour.maxValue = 24

        binding.numPickerMinute.minValue = 0
        binding.numPickerMinute.maxValue = 59


        //alarm seçmek için spinner
        val spinner1: Spinner = findViewById(R.id.spinnerAlarmSound)
        ArrayAdapter.createFromResource(
            this,
            R.array.alarm_sounds,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner1.adapter = adapter
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

        var min: Int = 0
        var sec: Int = 0

        binding.numPickerHour.setOnValueChangedListener { numberPicker, i, i2 ->
            min = numberPicker.value
        }
        binding.numPickerMinute.setOnValueChangedListener { numberPicker, i, i2 ->
            sec = numberPicker.value
        }

        val saveButton = findViewById<Button>(R.id.saveButton)
        saveButton.setOnClickListener{
            val day = binding.datePicker.dayOfMonth
            val month = binding.datePicker.month
            val year = binding.datePicker.year
            val hour = min
            val minute = sec
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