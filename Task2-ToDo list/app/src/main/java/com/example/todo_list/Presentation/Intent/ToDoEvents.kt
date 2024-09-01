package com.example.todo_list.Presentation.Intent

import com.example.todo_list.Data.TaskInfo

sealed interface ToDoEvents {
    data class AddTask(val task : TaskInfo) : ToDoEvents

    data class DeleteTask(val task: TaskInfo) : ToDoEvents

    data class DeleteTaskList(val tasks: List<TaskInfo>) : ToDoEvents

    data class addToCheckList(val task: TaskInfo) : ToDoEvents

    data class addListToChecklist(val taskList : List<TaskInfo>) : ToDoEvents

    data class removeFromCheckList(val task: TaskInfo) : ToDoEvents

    data class searchFor(val text : String) : ToDoEvents

    data object clearCheckList : ToDoEvents

    data class updateCheckList(val index : Int , val newTask : TaskInfo) : ToDoEvents

    data object ShowDialog : ToDoEvents

    data class showEditDialog(val task: TaskInfo) : ToDoEvents

    data object HideEditDialog : ToDoEvents

    data object HideDialog : ToDoEvents
}