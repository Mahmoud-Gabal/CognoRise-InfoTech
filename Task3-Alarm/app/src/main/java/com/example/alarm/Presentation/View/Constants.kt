package com.example.alarm.Presentation.View

import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.SaverScope
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import com.example.alarm.Data.AlarmData
import java.util.Timer
import java.util.TimerTask

object Constants {

    val daysOfWeek = listOf("Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday")



//    custom saver for mutableStatelist of String
    val saver = object : Saver<SnapshotStateList<String>, MutableList<String>> {
        override fun restore(value: MutableList<String>): SnapshotStateList<String>? {
            return value.toMutableStateList()
        }

        override fun SaverScope.save(value: SnapshotStateList<String>): MutableList<String>? {
            val savedList = mutableListOf<String>()
            for (item in value){
                savedList.add(item)
            }
            return savedList
        }
    }

//    custom saver for mutableStatelist of AlarmData
val alarmDataSaver = object : Saver<SnapshotStateList<AlarmData>, MutableList<AlarmData>> {
    override fun restore(value: MutableList<AlarmData>): SnapshotStateList<AlarmData>? {
        return value.toMutableStateList()
    }

    override fun SaverScope.save(value: SnapshotStateList<AlarmData>): MutableList<AlarmData>? {
        val savedList = mutableListOf<AlarmData>()
        for (item in value){
            savedList.add(item)
        }
        return savedList
    }
}
}