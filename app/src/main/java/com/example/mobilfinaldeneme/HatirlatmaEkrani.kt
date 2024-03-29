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
            // Db den sirasi gelen alarmi al
            val alarm = db.getAlarmByID(alarmId)
            // hatirlatici bilgilerini siradaki id hangisi ise onu al
            binding.baslik.text = "${alarm.day}/${alarm.month + 1}/${alarm.year}"
            binding.saat.text = "${alarm.hour}:${alarm.minute}"
            binding.aciklama.text = alarm.explanation
            alarmSound = alarm.sound
        }
        //alarm sesi ringtone ise
        if (alarmSound == "ringtone") {
            // Raw klasorunden ringtone adindaki ses dosyasini mediaplayere ata
            mediaPlayer = MediaPlayer.create(this, R.raw.ringtone)
            mediaPlayer.setOnPreparedListener {
                // Oynatmaya basla
                mediaPlayer.start()
            }
            mediaPlayer.setOnCompletionListener {
                // Medya oynatıcıyı serbest bırak
                mediaPlayer.release()
            }
        }
        //Alarma caldiginda alarmi kapatmak icin button
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
        //hareket sensorune aktiviteyi kaydet
        sensorManager.registerListener(this, sensorShake, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause() {
        super.onPause()
        //hareket sensorunden aktiviteyi kaldir
        sensorManager.unregisterListener(this)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    //alarm caldiginda telefon sallandigi zaman kapanir
    override fun onSensorChanged(event: SensorEvent?) {
        val db: AlarmDatabaseHelper by lazy { AlarmDatabaseHelper(this) }
        // Eğer olay varsa ve sensör tipi İvmeölçer se devam et
        if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]

            val acceleration = Math.sqrt((x * x + y * y + z * z).toDouble())
            // Eğer ivme 12'den büyükse, alarmı kapanir
            if (acceleration > 12) {
                Toast.makeText(this, "Ses Kapatıldı!", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                mediaPlayer.stop()
                //arkaplanda servisi durdurur
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