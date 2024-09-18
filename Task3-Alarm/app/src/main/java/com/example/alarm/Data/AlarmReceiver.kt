@file:OptIn(DelicateCoroutinesApi::class)

package com.example.alarm.Data

import android.Manifest
import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.os.Parcelable
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.collectAsState
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.alarm.Domain.AlarmNotificationChannel
import com.example.alarm.Domain.AlarmUseCases.AlarmCases
import com.example.alarm.Domain.Dao
import com.example.alarm.Presentation.Intent.SetAlarmViewModel
import com.example.alarm.R
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.Date
import javax.inject.Inject


const val Stop = "Stop"
const val Snooze = "Snooze"

@AndroidEntryPoint
class AlarmReceiver : BroadcastReceiver() {

    @Inject
    lateinit var dao: Dao

    @Inject
    lateinit var alarmCases: AlarmCases

    @Inject
    lateinit var alarmMediaPlayer: AlarmMediaPlayer


    @SuppressLint("SimpleDateFormat")
    override fun onReceive(context: Context, intent: Intent) {
        val alarmJson = intent.getStringExtra("Extra_alarm")
        val alarm = Gson().fromJson(alarmJson, AlarmData::class.java)
        val time = SimpleDateFormat("hh:mm a").format(Date(alarm.timeInMillis))

        val stopIntent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("Extra_alarm", alarmJson)
            action = Stop
        }
        val snoozeIntent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("Extra_alarm", alarmJson)
            action = Snooze
        }
        val stopPendingIntent = PendingIntent.getBroadcast(
            context,
            alarm.timeInMillis.toInt(),
            stopIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val snoozePendingIntent = PendingIntent.getBroadcast(
            context,
            alarm.timeInMillis.toInt(),
            snoozeIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        when (intent.action) {
            Stop -> {
                runBlocking {
                    NotificationManagerCompat.from(context)
                        .cancel("notification", alarm.hourMultiplyMinute)
                    val AllAlarms = dao.getAlarms().firstOrNull { it.isNotEmpty() }
                    val startedAlarmsWithSameTime =
                        AllAlarms?.filter { it.hourMultiplyMinute == alarm.hourMultiplyMinute }
                    for (startedAlarm in startedAlarmsWithSameTime!!) {
                        when (startedAlarm.ringTone) {
                            R.raw.drama -> alarmMediaPlayer.stopDrama()
                            R.raw.night -> alarmMediaPlayer.stopNight()
                            R.raw.funny -> alarmMediaPlayer.stopFunny()
                            R.raw.dream -> alarmMediaPlayer.stopDream()
                        }
                    }
                }
            }

            Snooze -> {
                runBlocking {
                    NotificationManagerCompat.from(context)
                        .cancel("notification", alarm.hourMultiplyMinute)
                    val AllAlarms = dao.getAlarms().firstOrNull { it.isNotEmpty() }
                    val startedAlarmsWithSameTime =
                        AllAlarms?.filter { it.hourMultiplyMinute == alarm.hourMultiplyMinute }
                    for (startedAlarm in startedAlarmsWithSameTime!!) {
                        when (startedAlarm.ringTone) {
                            R.raw.drama -> alarmMediaPlayer.stopDrama()
                            R.raw.night -> alarmMediaPlayer.stopNight()
                            R.raw.funny -> alarmMediaPlayer.stopFunny()
                            R.raw.dream -> alarmMediaPlayer.stopDream()
                        }
                    }
                }
                alarmCases.ScheduleCase(alarm.copy(timeInMillis = alarm.timeInMillis.plus(2*60*1000L)))
                Toast.makeText(context, "Snoozed, it will ring after 2 minutes", Toast.LENGTH_SHORT).show()
            }

            else -> {
                if (ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.POST_NOTIFICATIONS
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    val notification = NotificationCompat.Builder(context, AlarmNotificationChannel)
                        .setSmallIcon(R.drawable.bell)
                        .setContentTitle("Alarm is ringing")
                        .setContentText("$time \n ${alarm.description}")
                        .setContentInfo("this is from alarm $time")
                        .addAction(
                            R.drawable.ic_launcher_foreground,
                            "Dismiss",
                            stopPendingIntent
                        )
                        .addAction(
                            R.drawable.ic_launcher_foreground,
                            Snooze,
                            snoozePendingIntent
                        )
                        .setOngoing(true)
                        .build()
                    NotificationManagerCompat.from(context)
                        .notify("notification", alarm.hourMultiplyMinute, notification)
                }

                when (alarm.ringTone) {
                    R.raw.drama -> {
                        alarmMediaPlayer.startDrama()
                        runBlocking {
                            if (alarm.newTimeMillis == -1L || alarm.newTimeMillis == -2L){
                                dao.updateAlarm(alarm.copy(status = false))
                            }else{
                                val Alarms = dao.getAlarms().firstOrNull { it.isNotEmpty() }
                                val startedAlarmsWithSameTime =
                                    Alarms?.filter { it.newTimeMillis == alarm.newTimeMillis }
                                for (startedAlarm in startedAlarmsWithSameTime!!) {
                                    dao.updateAlarm(startedAlarm.copy(status = false))
                                }
                            }
                        }
                        GlobalScope.launch {
                            delay(alarmMediaPlayer.mediaPlayerNight.duration.toLong())
                            alarmCases.StopRingingCase(alarm)
                        }
                    }

                    R.raw.night -> {

                        alarmMediaPlayer.startNight()
                        runBlocking {
                            if (alarm.newTimeMillis == -1L || alarm.newTimeMillis == -2L){
                                dao.updateAlarm(alarm.copy(status = false))
                            }else{
                                val Alarms = dao.getAlarms().firstOrNull { it.isNotEmpty() }
                                val startedAlarmsWithSameTime =
                                    Alarms?.filter { it.newTimeMillis == alarm.newTimeMillis }
                                for (startedAlarm in startedAlarmsWithSameTime!!) {
                                    dao.updateAlarm(startedAlarm.copy(status = false))
                                }
                            }
                        }
                        GlobalScope.launch {
                            delay(alarmMediaPlayer.mediaPlayerNight.duration.toLong())
                            alarmCases.StopRingingCase(alarm)
                        }

                    }

                    R.raw.funny -> {
                        alarmMediaPlayer.startFunny()
                        runBlocking {
                            if (alarm.newTimeMillis == -1L || alarm.newTimeMillis == -2L){
                                dao.updateAlarm(alarm.copy(status = false))
                            }else{
                                val Alarms = dao.getAlarms().firstOrNull { it.isNotEmpty() }
                                val startedAlarmsWithSameTime =
                                    Alarms?.filter { it.newTimeMillis == alarm.newTimeMillis }
                                for (startedAlarm in startedAlarmsWithSameTime!!) {
                                    dao.updateAlarm(startedAlarm.copy(status = false))
                                }
                            }
                        }
                        GlobalScope.launch {
                            delay(alarmMediaPlayer.mediaPlayerFunny.duration.toLong())
                            alarmCases.StopRingingCase(alarm)
                        }

                    }

                    R.raw.dream -> {
                        alarmMediaPlayer.startDream()
                        runBlocking {
                            if (alarm.newTimeMillis == -1L || alarm.newTimeMillis == -2L){
                                dao.updateAlarm(alarm.copy(status = false))
                            }else{
                                val Alarms = dao.getAlarms().firstOrNull { it.isNotEmpty() }
                                val startedAlarmsWithSameTime =
                                    Alarms?.filter { it.newTimeMillis == alarm.newTimeMillis }
                                for (startedAlarm in startedAlarmsWithSameTime!!) {
                                    dao.updateAlarm(startedAlarm.copy(status = false))
                                }
                            }
                        }
                        GlobalScope.launch {
                            delay(alarmMediaPlayer.mediaPlayerDream.duration.toLong())
                            alarmCases.StopRingingCase(alarm)
                        }
                    }
                }
            }
        }
    }


}