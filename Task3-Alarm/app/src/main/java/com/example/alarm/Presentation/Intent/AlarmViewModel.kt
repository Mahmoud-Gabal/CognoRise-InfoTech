package com.example.alarm.Presentation.Intent

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alarm.Domain.Dao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlarmViewModel @Inject constructor(
    val dao : Dao
) : ViewModel() {

    val alarmList = dao.getAlarms()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun onEvent(event: AlarmEvents){
        when(event){
            is AlarmEvents.AddAlarm -> {
                viewModelScope.launch {
                    dao.addOrUpdateAlarm(event.alarm)
                }
            }
            is AlarmEvents.RemoveAlarm -> {
                viewModelScope.launch {
                    dao.deleteAlarm(event.alarm)
                }
            }
            is AlarmEvents.RemoveAlarmsList -> {
                viewModelScope.launch {
                    dao.deleteAlarmsList(event.alarms)
                }
            }
            is AlarmEvents.UpdateAlarm -> {
                viewModelScope.launch {
                    dao.updateAlarm(event.alarm)
                }
            }
        }
    }

}