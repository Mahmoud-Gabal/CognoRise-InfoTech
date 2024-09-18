package com.example.alarm.Domain

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import com.example.alarm.Data.AlarmDB
import com.example.alarm.Data.AlarmMediaPlayer
import com.example.alarm.Data.AlarmScheduler
import com.example.alarm.Domain.AlarmUseCases.AlarmCases
import com.example.alarm.Domain.AlarmUseCases.ScheduleCase
import com.example.alarm.Domain.AlarmUseCases.StopCase
import com.example.alarm.Domain.AlarmUseCases.StopRingingCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideAlarmDB(@ApplicationContext context : Context)  : AlarmDB = Room.databaseBuilder(
        context = context,
        AlarmDB::class.java,
        "alarm_db"
    ).fallbackToDestructiveMigration()
        .build()


    @Singleton
    @Provides
    fun provideAlarmDao(alarmDB : AlarmDB) : Dao = alarmDB.Dao



    @Singleton
    @Provides
    fun provideAlarmMediaPlayer(@ApplicationContext context: Context)  : AlarmMediaPlayer = AlarmMediaPlayer(context)

    @Singleton
    @Provides
    fun provideAlarmScheduler(@ApplicationContext context : Context,alarmMediaPlayer: AlarmMediaPlayer) : AlarmScheduler = AlarmScheduler(context,alarmMediaPlayer)

    @Singleton
    @Provides
    fun provideAlarmCases(alarmScheduler: AlarmScheduler) : AlarmCases =
        AlarmCases(
            ScheduleCase = ScheduleCase(alarmScheduler),
            StopCase = StopCase(alarmScheduler),
            StopRingingCase = StopRingingCase(alarmScheduler)
        )

}