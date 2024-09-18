package com.example.alarm.Data

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.alarm.Domain.Dao

@Database(
    entities = [AlarmData::class],
    version = 9,
)
abstract class AlarmDB : RoomDatabase() {
    abstract val Dao : Dao
}

