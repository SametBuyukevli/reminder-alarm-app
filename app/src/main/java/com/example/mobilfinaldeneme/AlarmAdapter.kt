package com.example.mobilfinaldeneme

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView


class AlarmAdapter(private var alarms:List<AlarmDb>, context:Context) :
    RecyclerView.Adapter<AlarmAdapter.AlarmViewHolder>() {



    class AlarmViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dayMonthYearTextView: TextView = itemView.findViewById(R.id.tarih)
        val timeTextView: TextView = itemView.findViewById(R.id.saat)
        val explanationTextView: TextView = itemView.findViewById(R.id.aciklamaa)

        //val editButton : ImageView = itemView.findViewById(R.id.edit_button)

        fun bind(alarmData: AlarmDb) {
            // Gün, ay ve yılı tek bir dize olarak birleştir
            val dateText = "${alarmData.day} ${getMonthName(alarmData.month)} ${alarmData.year}"
            dayMonthYearTextView.text = dateText
            timeTextView.text = "${alarmData.hour}:${alarmData.minute}"
            explanationTextView.text = alarmData.explanation
        }
        // Ay adını almak icin bir fonksiyon
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
        val view = LayoutInflater.from(parent.context).inflate(R.layout.alarm_item,parent,false)
        return AlarmViewHolder(view)
    }

    override fun getItemCount(): Int = alarms.size

    override fun onBindViewHolder(holder: AlarmViewHolder, position: Int) {
        val alarm = alarms[position]
        holder.bind(alarm)

    }



    fun refreshData(newAlarm:List<AlarmDb>){
        alarms = newAlarm
        notifyDataSetChanged()
        Log.d("AlarmDeleteAdapter", "Data refreshed. New size: ${alarms.size}")

    }
}