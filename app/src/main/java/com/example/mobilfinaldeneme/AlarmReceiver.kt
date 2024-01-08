package com.example.mobilfinaldeneme

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.example.mobilfinaldeneme.databinding.HatirlatmaEkraniBinding
import java.util.Calendar

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent) {
        if (context != null && intent != null) {
            val alarmId = intent.getIntExtra("ALARM_ID", -1)
            if (alarmId != -1) {

                val db = AlarmDatabaseHelper(context)
                val alarm = db.getAlarmByID(alarmId)
                val calendar = Calendar.getInstance()
                val currentMillis = calendar.timeInMillis
                calendar.set(Calendar.HOUR_OF_DAY, alarm.hour)
                calendar.set(Calendar.MINUTE, alarm.minute)
                val alarmMillis = calendar.timeInMillis

                //sistemdeki saat ile karşılaştırma yapar eğer saati geçmiş ise o alarımı geçer
                if (currentMillis > alarmMillis) {
                    Log.d(
                        "AlarmReceiver",
                        "Saati Geçen Alarm ==> tarih: ${alarm.day}/${alarm.month + 1}/${alarm.year} saat: ${alarm.hour}:${alarm.minute} sistemin saati: ${calendar.get(Calendar.HOUR_OF_DAY)}:${calendar.get(Calendar.MINUTE)}"
                    )
                    return
                } else {
                    Log.d(
                        "AlarmReceiver",
                        "Çalan alarm ==> tarih: ${alarm.day}/${alarm.month + 1}/${alarm.year} saat: ${alarm.hour}:${alarm.minute} sistemin saati: ${calendar.get(Calendar.HOUR_OF_DAY)}:${calendar.get(Calendar.MINUTE)}"
                    )
                    Toast.makeText(
                        context,
                        "Alarm çalıyor: ${alarm.explanation}",
                        Toast.LENGTH_SHORT
                    ).show()

                    val intent = Intent(context, HatirlatmaEkrani::class.java)
                    intent.putExtra("ALARM_ID", alarmId)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                    context.startActivity(intent)
                }
            }
        }
    }
}
