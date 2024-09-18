package com.example.alarm.Presentation.Intent

import com.example.alarm.Data.AlarmData

interface SetAlarmEvents {

//    Actions for repeat
    data class AddDay(val day: String) : SetAlarmEvents

    data class AddDaysList(val days: List<String>) : SetAlarmEvents

    data class RemoveDay(val day: String) : SetAlarmEvents

    object ResetDays : SetAlarmEvents

//  Actions for alarm Data
    data class AddDescription(val description: String) : SetAlarmEvents

    data class AddTime(val timeInMills : Long) : SetAlarmEvents

    data class AddAdditionalInfo(val additionalInfo: String) : SetAlarmEvents

    data class AddStatus(val status: Boolean) : SetAlarmEvents

    data class AddSnoozeMode(val isSnoozed : Boolean) : SetAlarmEvents

    data class AddRingTone(val ringTone: Int) : SetAlarmEvents
    data class AddHourMultiplyMinute(val result  : Int)  : SetAlarmEvents
    data class AddHour(val hour :Int) : SetAlarmEvents
    data class AddMinute(val minute : Int) : SetAlarmEvents
    data class AddNewTimeMillis(val newTimeMillis : Long) :SetAlarmEvents
    data class AddTimeInText(val time : String)  : SetAlarmEvents


    data class AddWholeAlarm(val alarm: AlarmData) : SetAlarmEvents

    data class SetOldAlarm(val alarm: AlarmData) : SetAlarmEvents
    object ResetOldAlarm : SetAlarmEvents

    object ResetAlarmData : SetAlarmEvents

//    Actions for schedule or stop alarm

     data class ScheduleAlarm(val alarm : AlarmData) : SetAlarmEvents
     data class StopAlarm(val alarm : AlarmData) : SetAlarmEvents

}