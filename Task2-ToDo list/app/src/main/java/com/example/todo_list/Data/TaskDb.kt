package com.example.todo_list.Data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.todo_list.Domain.Dao


@Database(
    entities = [TaskInfo::class],
    version = 1
)
abstract class TaskDb : RoomDatabase() {
    abstract val Dao : Dao
}