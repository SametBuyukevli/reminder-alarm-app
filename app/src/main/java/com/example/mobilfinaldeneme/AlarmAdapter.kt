package com.example.mobilfinaldeneme

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import java.lang.reflect.Array.set
import java.util.Calendar


class AlarmAdapter(private var alarms:List<AlarmDb>, private val context:Context) :
    RecyclerView.Adapter<AlarmAdapter.AlarmViewHolder>() {

    class AlarmViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dayMonthYearTextView: TextView = itemView.findViewById(R.id.tarih)
        val timeTextView: TextView = itemView.findViewById(R.id.saat)
        val explanationTextView: TextView = itemView.findViewById(R.id.aciklamaa)

        fun bind(alarmData: AlarmDb) {

            // Gün, ay ve yılı tek bir dize olarak birleştir
            val dateText = "${alarmData.day} ${getMonthName(alarmData.month)} ${alarmData.year}"
            dayMonthYearTextView.text = dateText
            timeTextView.text = "${alarmData.hour}:${alarmData.minute}"
            explanationTextView.text = alarmData.explanation
            Log.d(
                "AlarmApp",
                "Eklenen ay: ${getMonthName(alarmData.month)}"
            ) // Log mesajı olarak hangi ayın eklendiğini gösterir

        }

        // Ay adını almak icin bir fonksiyon
        private fun getMonthName(month: Int): String {
            return when (month) {
                0 -> "Ocak"
                1 -> "Şubat"
                2 -> "Mart"
                3 -> "Nisan"
                4 -> "Mayıs"
                5 -> "Haziran"
                6 -> "Temmuz"
                7 -> "Ağustos"
                8 -> "Eylül"
                9 -> "Ekim"
                10 -> "Kasım"
                11 -> "Aralık"
                else -> "Bilinmeyen Ay"
            }
        }
    }

    // Yeni bir View oluşturulduğunda çağrılır
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.alarm_item, parent, false)
        return AlarmViewHolder(view)
    }

    override fun getItemCount(): Int = alarms.size
    //veri kümseinde toplam item sayısını döndürür
    override fun onBindViewHolder(holder: AlarmViewHolder, position: Int) {
        // kayıt yapılan alarmları görüntüler
        val alarm = alarms[position]
        holder.bind(alarm)
        Log.d(
            "AlarmAdapter",
            "Binding alarm at position $position, Time: ${alarm.day}/${alarm.month + 1}/${alarm.year} ${alarm.hour}:${alarm.minute}"
        )
        scheduleAlarm(alarm)
    }


    private fun scheduleAlarm(alarm: AlarmDb) {
        Log.d(
            "AlarmScheduler",
            "Scheduling alarm for ${alarm.day}/${alarm.month + 1}/${alarm.year} ${alarm.hour}:${alarm.minute}"
        )
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)

        // intent nesnesine alarmid ekledik hangi alarma ait oldugunu bilmesi icin
        intent.putExtra("ALARM_ID", alarm.id)

        //eylemin gerceklesmesi icin pendingIntent olusturduk
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            alarm.id,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        //val intervalMillis: Long =  60000 // 1 dakika
        // alarm zamani ayarlamak icin calendar sinifini kullandik
        // Eğer alarm daha önce planlanmamışsa veya tarihi geçmişse planla
        if (!alarm.isScheduled(context) || alarm.isPast()) {
            //alarm zamani ayarlamak icin calendar sinifini kullandik
            val calendar = Calendar.getInstance().apply {
                set(alarm.year, alarm.month, alarm.day, alarm.hour, alarm.minute, 0)
            }

            // API >= 19 olan cihazlar için setExact, API < 19 için set kullan
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                alarmManager.setExact(
                    //alarm gercek zaman saati kullanilarak ayarlanacagini belirtiyor
                    AlarmManager.RTC_WAKEUP,
                    //tarih ve saat bilgilierini saklar
                    calendar.timeInMillis,
                    //BoradcastReceiver tetikler
                    pendingIntent
                )
            } else {
                alarmManager.set(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )
            }
        }
    }


    fun refreshData(newAlarm: List<AlarmDb>) {
        alarms = newAlarm
        notifyDataSetChanged()
        Log.d("AlarmDeleteAdapter", "Data refreshed. New size: ${alarms.size}")

    }
}
