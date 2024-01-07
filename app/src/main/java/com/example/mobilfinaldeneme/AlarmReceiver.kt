package com.example.mobilfinaldeneme

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.CalendarView
import android.widget.Toast
import java.util.Calendar
import java.util.TimeZone

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent) {
        if (context != null && intent != null) {
            val alarmId = intent.getIntExtra("ALARM_ID", -1)
            if (alarmId != -1) {
                // ID'ye göre veritabanından alarmı alın
                val db = AlarmDatabaseHelper(context)
                val alarm = db.getAlarmByID(alarmId)
                val calendar = Calendar.getInstance()
                val hour = calendar.get(Calendar.HOUR_OF_DAY)
                val minute = calendar.get(Calendar.MINUTE)
                // Alarm temelinde işlemleri gerçekleştirin
                Log.d("AlarmReceiver", "Çalan alarm ==> tarih: ${alarm.day}/${alarm.month + 1}/${alarm.year} saat: ${alarm.hour}:${alarm.minute} sistemin saati: ${hour}:${minute}")
                Toast.makeText(context, "Alarm çalıyor: ${alarm.explanation}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

