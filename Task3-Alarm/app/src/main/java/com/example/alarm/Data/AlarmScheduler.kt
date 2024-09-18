package com.example.alarm.Data

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.app.NotificationManagerCompat
import com.example.alarm.R
import com.google.gson.Gson
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class AlarmScheduler (
    val context: Context,
    val alarmMediaPlayer: AlarmMediaPlayer
){
    val alarmMg = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        fun schedule(alarm: AlarmData) {
            val intent = Intent(context,AlarmReceiver::class.java).apply {
                putExtra("Extra_alarm",Gson().toJson(alarm))
            }
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                alarm.timeInMillis.toInt(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            try {
                alarmMg.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    alarm.timeInMillis,
                    pendingIntent
                )
            }catch (e : SecurityException){
                Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
            }
        }

        fun stopRinging(alarm: AlarmData) {
            NotificationManagerCompat.from(context).cancel("notification",alarm.hourMultiplyMinute)
            when(alarm.ringTone){
                R.raw.drama -> alarmMediaPlayer.stopDrama()
                R.raw.night -> alarmMediaPlayer.stopNight()
                R.raw.funny -> alarmMediaPlayer.stopFunny()
                R.raw.dream -> alarmMediaPlayer.stopDream()
            }
        }

        fun stop(alarm: AlarmData) {
            val intent = Intent(context,AlarmReceiver::class.java).apply {
                putExtra("Extra_alarm",Gson().toJson(alarm))
            }
            alarmMg.cancel(
                PendingIntent.getBroadcast(
                    context,
                    alarm.timeInMillis.toInt(),
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
            )
        }


}