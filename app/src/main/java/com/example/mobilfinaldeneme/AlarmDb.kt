package com.example.mobilfinaldeneme

import android.content.Context
import java.util.Calendar

data class AlarmDb(
    val id:Int,
    val day: Int,
    val month: Int,
    val year: Int,
    val hour: Int,
    val minute: Int,
    val sound: String,
    val explanation: String,
    val reminder: String
)
{
    // Alarmın daha önce planlanıp planlanmadığını kontrol eder
    fun isScheduled(context: Context): Boolean {
        val dbHelper = AlarmDatabaseHelper(context)
        val alarm = dbHelper.getAlarmByID(id)
        // Burada gerçek kontrolü yapmalısınız.
        // Örneğin, veritabanınızda bu alarmın bir planlanma durumu tutuluyorsa,
        // bu durumu kontrol edebilirsiniz. Örneğin:
        // return alarm.isScheduledInDatabase()
        return false
    }

    // Alarmın tarihinin geçip geçmediğini kontrol eder
    fun isPast(): Boolean {
        val currentCalendar = Calendar.getInstance()
        val alarmCalendar = Calendar.getInstance().apply {
            set(year, month - 1, day, hour, minute, 10)
        }
        return currentCalendar.timeInMillis > alarmCalendar.timeInMillis
    }
}
