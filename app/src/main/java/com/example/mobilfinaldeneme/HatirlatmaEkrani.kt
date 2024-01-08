package com.example.mobilfinaldeneme

import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mobilfinaldeneme.databinding.HatirlatmaEkraniBinding


class HatirlatmaEkrani : AppCompatActivity() {
    private lateinit var binding: HatirlatmaEkraniBinding
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var alarmSound: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = HatirlatmaEkraniBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val alarmId = intent.getIntExtra("ALARM_ID", -1)
        if (alarmId != -1) {
            val db = AlarmDatabaseHelper(this)
            val alarm = db.getAlarmByID(alarmId)
            // Alarm bilgilerini kullanarak gerekli işlemleri yapın
            binding.baslik.text = "${alarm.day}/${alarm.month + 1}/${alarm.year}"
            binding.saat.text = "${alarm.hour}:${alarm.minute}"
            binding.aciklama.text = alarm.explanation
            alarmSound = alarm.sound
        }

        val alarmCalisiyorMu = intent.getBooleanExtra("ALARM_CALISIYOR_MU", false)
        if (alarmCalisiyorMu) {
            if (alarmSound == "ringtone") {
                mediaPlayer = MediaPlayer.create(this, R.raw.ringtone)
                mediaPlayer.setOnPreparedListener {
                    mediaPlayer.start()
                }
                mediaPlayer.setOnCompletionListener {
                    mediaPlayer.release()
                }
            }
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
    }
}