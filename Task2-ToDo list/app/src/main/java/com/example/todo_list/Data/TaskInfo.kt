package com.example.todo_list.Data

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class TaskInfo(
    val title : String,
    val description : String,
    @PrimaryKey(autoGenerate = true) val key : Int = 0
)