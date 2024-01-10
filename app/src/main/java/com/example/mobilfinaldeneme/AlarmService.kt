package com.example.mobilfinaldeneme
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.widget.Toast
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

@Suppress("DEPRECATION")
class AlarmService : Service() {

    private lateinit var mediaPlayer: MediaPlayer
    private val NOTIFICATION_ID = 12345


    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        mediaPlayer = MediaPlayer.create(this, R.raw.ringtone)
        mediaPlayer.isLooping = true
        Log.d("alarmoluştu","alarmoluşturuldu")

        // Bildirim kanalını oluşturma (sadece Android Oreo ve üstü için)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "alarm_channel"
            val channelName = "Alarm Channel"
            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH).apply {

                enableVibration(true)
            }

            val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

    }




    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val intentToYourActivity = Intent(this, HatirlatmaEkrani::class.java)
        intentToYourActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntentToYourActivity = PendingIntent.getActivity(
            this,
            0,
            intentToYourActivity,
            PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE,

        )
        mediaPlayer.start()
        Log.d("AlarmServicee","AlarmServis başlatıldı")
        val channelId = "alarm_channel"
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("Alarm Çalıyor!")
            .setContentText("Alarmınız şu anda çalıyor.")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .addAction(R.drawable.baseline_menu_24,"Alarma git",pendingIntentToYourActivity)
            .build()

        startForeground(NOTIFICATION_ID, notificationBuilder)

        return START_STICKY

    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.stop()

        stopForeground(true)

    }

}