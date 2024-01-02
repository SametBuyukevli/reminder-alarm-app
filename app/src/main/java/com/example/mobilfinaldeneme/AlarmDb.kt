package com.example.mobilfinaldeneme
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
