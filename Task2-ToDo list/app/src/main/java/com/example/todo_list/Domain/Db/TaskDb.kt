package com.example.todo_list.Domain.Db

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(
    entities = [TaskInfo::class],
    version = 1
)
abstract class TaskDb : RoomDatabase() {
    abstract val Dao : Dao
}