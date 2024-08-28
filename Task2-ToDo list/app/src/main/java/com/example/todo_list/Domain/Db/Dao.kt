package com.example.todo_list.Domain.Db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow


@Dao
interface Dao {

    @Upsert
    suspend fun addTask(taskInfo: TaskInfo)

    @Delete
    suspend fun deleteTask(taskInfo: TaskInfo)

    @Delete
    suspend fun deleteTasksList(taskInfo: List<TaskInfo>)

    @Query("select * from taskinfo order by `key` Asc")
    fun getAllTasks() : Flow<List<TaskInfo>>

}