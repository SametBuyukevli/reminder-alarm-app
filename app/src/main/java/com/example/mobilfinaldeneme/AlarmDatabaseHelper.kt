package com.example.mobilfinaldeneme

import android.app.DownloadManager.Query
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


class AlarmDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {


    companion object {
        private const val DATABASE_NAME = "reminderalarm.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "alarm"
        private const val COLUMN_ID = "id"      //auto-inc
        private const val COLUMN_DAY = "day"
        private const val COLUMN_MONTH = "month"
        private const val COLUMN_YEAR = "year"
        private const val COLUMN_HOUR = "hour"
        private const val COLUMN_MINUTE = "minute"
        private const val COLUMN_SOUND = "sound"
        private const val COLUMN_EXPLANATION = "explanation"
        private const val COLUMN_REMINDER = "reminder"
    }

    //verilerimizn icersinde olucagi tabloyu olusturduk
    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = """
            CREATE TABLE $TABLE_NAME (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_DAY INTEGER,
                $COLUMN_MONTH INTEGER,
                $COLUMN_YEAR INTEGER,
                $COLUMN_HOUR INTEGER,
                $COLUMN_MINUTE INTEGER,
                $COLUMN_SOUND STRING,
                $COLUMN_EXPLANATION STRING,
                $COLUMN_REMINDER STRING
            )
            """
        //db degeri null degilse execSQL icerisindeki kodu calistir
        db?.execSQL(createTableQuery)
    }
    // bu kısım tablo eğer yoksa yeninden oluşturma kontorl kısmı
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, NewVersion: Int) {
        val dropTableQuery = "DROP TABLE IF EXISTS $TABLE_NAME"
        db?.execSQL(dropTableQuery)
        onCreate(db)
    }

    //gorevEkle.kt icersinden argumani aliyoruz ve insert ile db kayit ediyoruz
    fun insertAlarm(alarmDb: AlarmDb) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_DAY, alarmDb.day)
            put(COLUMN_MONTH, alarmDb.month)
            put(COLUMN_YEAR, alarmDb.year)
            put(COLUMN_HOUR, alarmDb.hour)
            put(COLUMN_MINUTE, alarmDb.minute)
            put(COLUMN_SOUND, alarmDb.sound)
            put(COLUMN_EXPLANATION, alarmDb.explanation)
            put(COLUMN_REMINDER, alarmDb.reminder)
        }
        //table name gore kayiti olusturur
        db.insert(TABLE_NAME, null, values)
        db.close()
    }
    //eklenen tüm alarmları çekme
    fun getAllAlarms(): List<AlarmDb> {
        val alarmList = mutableListOf<AlarmDb>()
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_NAME"
        val cursor = db.rawQuery(query, null)

        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
            val day = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_DAY))
            val month = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_MONTH))
            val year = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_YEAR))
            val hour = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_HOUR))
            val minute = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_MINUTE))
            val sound = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SOUND))
            val explanation = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EXPLANATION))
            val reminder = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_REMINDER))

            val alarms = AlarmDb(id,day,month,year,hour,minute,sound,explanation,reminder)
            alarmList.add(alarms)
        }
        cursor.close()
        db.close()
        return alarmList
    }
    //alarm silme
    fun deleteAlarm(alarmId: Int){
        val db = writableDatabase
        val whereClause = "$COLUMN_ID = ?"
        val whereArgs = arrayOf(alarmId.toString())
        db.delete(TABLE_NAME,whereClause,whereArgs)
        db.close()
    }
    //Veritabanından belirli bir alarmı ID'ye göre getirir
    fun getAlarmByID(alarmID:Int):AlarmDb{
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_ID = $alarmID"
        // yazılan sorguyu çalıştır ve sonuçları al
        val cursor = db.rawQuery(query,null)
        cursor.moveToFirst()

        val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
        val day = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_DAY))
        val month = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_MONTH))
        val year = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_YEAR))
        val hour = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_HOUR))
        val minute = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_MINUTE))
        val sound = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SOUND))
        val explanation = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EXPLANATION))
        val reminder = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_REMINDER))

        cursor.close()
        db.close()
        return AlarmDb(id,day,month,year,hour,minute,sound,explanation,reminder)
    }
}


