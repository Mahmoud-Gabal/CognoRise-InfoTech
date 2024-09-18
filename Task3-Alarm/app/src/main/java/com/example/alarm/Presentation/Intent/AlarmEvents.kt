package com.example.alarm.Presentation.Intent

import com.example.alarm.Data.AlarmData

sealed interface AlarmEvents {
    data class AddAlarm(val alarm: AlarmData) : AlarmEvents

    data class UpdateAlarm(val alarm: AlarmData) : AlarmEvents

    data class RemoveAlarm(val alarm: AlarmData) : AlarmEvents

    data class RemoveAlarmsList(val alarms: List<AlarmData>) : AlarmEvents
}