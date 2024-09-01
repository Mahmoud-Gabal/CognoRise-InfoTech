package com.example.todo_list.Presentation.Model

import com.example.todo_list.Data.TaskInfo

data class ToDoStates(
    val showDialog : Boolean = false,
    val editTask : TaskInfo = TaskInfo("",""),
    val showEditDialog : Boolean = false,
    val appLoading : Boolean = true,
)
