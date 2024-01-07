package com.example.mobilfinaldeneme

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class AlarmDeleteAdapter(private var alarms: List<AlarmDb>, context: Context) :
    RecyclerView.Adapter<AlarmDeleteAdapter.AlarmViewHolder>() {

    private val db: AlarmDatabaseHelper = AlarmDatabaseHelper(context)

    class AlarmViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dayMonthYearTextView: TextView = itemView.findViewById(R.id.tarih)
        val timeTextView: TextView = itemView.findViewById(R.id.saat)
        val explanationTextView: TextView = itemView.findViewById(R.id.Aciklamaa)
        val deleteButton : ImageView = itemView.findViewById(R.id.deleteBtn)

        fun bind(alarmData: AlarmDb) {
            val dateText = "${alarmData.day} ${getMonthName(alarmData.month)} ${alarmData.year}"
            dayMonthYearTextView.text = dateText
            timeTextView.text = "${alarmData.hour}:${alarmData.minute}"
            explanationTextView.text = alarmData.explanation
        }

        private fun getMonthName(month: Int): String {
            val monthNames = arrayOf(
                "Ocak", "Şubat", "Mart", "Nisan", "Mayıs", "Haziran",
                "Temmuz", "Ağustos", "Eylül", "Ekim", "Kasım", "Aralık"
            )
            val adjustedMonth = if (month in 1..12) month else 1
            return monthNames[adjustedMonth]
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.alarm_item_delete, parent,false)
        return AlarmViewHolder(view)
    }

    override fun onBindViewHolder(holder: AlarmViewHolder, position: Int) {
        val alarm = alarms[position]
        holder.bind(alarm)

        holder.deleteButton.setOnClickListener{
            db.deleteAlarm(alarm.id)
            refreshData(db.getAllAlarms())
            Toast.makeText(holder.itemView.context,"Alarm Deleted",Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int = alarms.size

    fun refreshData(newAlarm:List<AlarmDb>){
        alarms = newAlarm
        notifyDataSetChanged()
    }
}