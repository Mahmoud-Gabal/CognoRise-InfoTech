package com.example.alarm.Presentation.Intent

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.alarm.Data.AlarmData
import com.example.alarm.Domain.AlarmUseCases.AlarmCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SetAlarmViewModel @Inject constructor(
    val alarmCases: AlarmCases
) : ViewModel() {

    private val _customDaysList = mutableStateListOf<String>()
    val customDaysList: List<String> = _customDaysList


    private val _alarmData = MutableStateFlow(AlarmData())
    val alarmData = _alarmData.asStateFlow()

    private val _oldAlarm = MutableStateFlow(AlarmData())
    val oldAlarm = _oldAlarm.asStateFlow()

    fun onSetAction(event: SetAlarmEvents) {
        when (event) {
            is SetAlarmEvents.AddDay -> _customDaysList.add(event.day)
            is SetAlarmEvents.RemoveDay -> _customDaysList.remove(event.day)
            is SetAlarmEvents.AddDaysList -> _customDaysList.addAll(event.days)
            is SetAlarmEvents.ResetDays -> _customDaysList.clear()
            is SetAlarmEvents.AddDescription -> _alarmData.update { it.copy(description = event.description) }
            is SetAlarmEvents.AddTime -> _alarmData.update { it.copy(timeInMillis = event.timeInMills) }
            is SetAlarmEvents.AddAdditionalInfo -> _alarmData.update { it.copy(additionalInfo = event.additionalInfo) }
            is SetAlarmEvents.AddStatus -> _alarmData.update { it.copy(status = event.status) }
            is SetAlarmEvents.AddSnoozeMode -> _alarmData.update { it.copy(snoozeMode = event.isSnoozed) }
            is SetAlarmEvents.AddRingTone -> _alarmData.update { it.copy(ringTone = event.ringTone) }
            is SetAlarmEvents.AddHourMultiplyMinute -> _alarmData.update { it.copy(hourMultiplyMinute = event.result) }
            is SetAlarmEvents.AddHour -> _alarmData.update { it.copy(hour = event.hour) }
            is SetAlarmEvents.AddMinute -> _alarmData.update { it.copy(minute = event.minute) }
            is SetAlarmEvents.AddNewTimeMillis -> _alarmData.update { it.copy(newTimeMillis = event.newTimeMillis) }
            is SetAlarmEvents.AddTimeInText ->_alarmData.update { it.copy(timeInText = event.time) }
            is SetAlarmEvents.AddWholeAlarm -> _alarmData.value = event.alarm
            is SetAlarmEvents.ResetAlarmData -> _alarmData.value = AlarmData()
            is SetAlarmEvents.SetOldAlarm -> _oldAlarm.value = event.alarm
            is SetAlarmEvents.ResetOldAlarm -> _oldAlarm.value = AlarmData()
            is SetAlarmEvents.ScheduleAlarm -> alarmCases.ScheduleCase(event.alarm)
            is SetAlarmEvents.StopAlarm -> alarmCases.StopCase(event.alarm)
        }
    }

}