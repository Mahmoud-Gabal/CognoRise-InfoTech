package com.example.alarm.Domain.AlarmUseCases

import com.example.alarm.Data.AlarmData
import com.example.alarm.Data.AlarmScheduler

class ScheduleCase(
    val alarmScheduler: AlarmScheduler
) {
    operator fun invoke(alarm: AlarmData) = alarmScheduler.schedule(alarm)
}