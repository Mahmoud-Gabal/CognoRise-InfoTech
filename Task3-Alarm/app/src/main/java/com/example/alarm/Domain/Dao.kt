package com.example.alarm.Domain

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.example.alarm.Data.AlarmData
import kotlinx.coroutines.flow.Flow

@Dao
interface Dao {

    @Upsert
    suspend fun addOrUpdateAlarm(alarm : AlarmData)

    @Update
    suspend fun updateAlarm(alarm: AlarmData)

    @Delete
    suspend fun deleteAlarm(alarm: AlarmData)

    @Delete
    suspend fun deleteAlarmsList(alarms : List<AlarmData>)

    @Query("select * from alarmdata order by timeInText Asc")
    fun getAlarms() : Flow<List<AlarmData>>

}