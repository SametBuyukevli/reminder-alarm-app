package com.example.mobilfinaldeneme

import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mobilfinaldeneme.databinding.HatirlatmaEkraniBinding

class HatirlatmaEkrani : AppCompatActivity(), SensorEventListener {
    private lateinit var binding: HatirlatmaEkraniBinding
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var alarmSound: String
    val db: AlarmDatabaseHelper by lazy { AlarmDatabaseHelper(this) }
    private lateinit var sensorManager: SensorManager
    private var sensorShake: Sensor? = null
    private var alarmId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = HatirlatmaEkraniBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensorShake = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        alarmId = intent.getIntExtra("ALARM_ID", -1)
        if (alarmId != -1) {

            val alarm = db.getAlarmByID(alarmId)
            // Perform necessary actions using alarm information
            binding.baslik.text = "${alarm.day}/${alarm.month + 1}/${alarm.year}"
            binding.saat.text = "${alarm.hour}:${alarm.minute}"
            binding.aciklama.text = alarm.explanation
            alarmSound = alarm.sound
        }

        if (alarmSound == "ringtone") {
            mediaPlayer = MediaPlayer.create(this, R.raw.ringtone)
            mediaPlayer.setOnPreparedListener {
                mediaPlayer.start()
            }
            mediaPlayer.setOnCompletionListener {
                mediaPlayer.release()
            }
        }

        binding.AlarmDurdurBtn.setOnClickListener() {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            mediaPlayer.stop()
            db.deleteAlarm(alarmId)
            finish()
            val stopServiceIntent = Intent(applicationContext, AlarmService::class.java)
            applicationContext.stopService(stopServiceIntent)
        }


    }


    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(this, sensorShake, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Do nothing
    }

    override fun onSensorChanged(event: SensorEvent?) {
        val db: AlarmDatabaseHelper by lazy { AlarmDatabaseHelper(this) }

        if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]

            val acceleration = Math.sqrt((x * x + y * y + z * z).toDouble())

            if (acceleration > 12) {
                Toast.makeText(this, "Ses Kapatıldı!", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                mediaPlayer.stop()
                val stopServiceIntent = Intent(applicationContext, AlarmService::class.java)
                applicationContext.stopService(stopServiceIntent)
                db.deleteAlarm(alarmId)
                finish()

            }


        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
    }


}