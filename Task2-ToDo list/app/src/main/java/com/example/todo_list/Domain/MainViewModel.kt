package com.example.todo_list.Domain

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todo_list.Domain.Db.Dao
import com.example.todo_list.Domain.Db.TaskInfo
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed interface ToDoEvents {
    data class AddTask(val task : TaskInfo) : ToDoEvents

    data class DeleteTask(val task: TaskInfo) : ToDoEvents

    data class DeleteTaskList(val tasks: List<TaskInfo>) : ToDoEvents

    data class addToCheckList(val task: TaskInfo) : ToDoEvents

    data class addListToChecklist(val taskList : List<TaskInfo>) : ToDoEvents

    data class removeFromCheckList(val task: TaskInfo) : ToDoEvents

    data object clearCheckList : ToDoEvents

    data object ShowDialog : ToDoEvents

    data object HideDialog : ToDoEvents
}

class MainViewModel(
    val Dao: Dao
) : ViewModel() {

    private var _showDialog = mutableStateOf(false)
    var showDialog : State<Boolean> = _showDialog

    val _checkList = mutableStateListOf<TaskInfo>()
    val checkList : List<TaskInfo> = _checkList

    var taskList = Dao.getAllTasks().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000),
        emptyList()
    )


     fun onEvent(event : ToDoEvents){
        when(event){
            is ToDoEvents.AddTask -> {
                viewModelScope.launch {
                    Dao.addTask(event.task)
                }
            }
            is ToDoEvents.DeleteTask -> {
                viewModelScope.launch {
                    Dao.deleteTask(event.task)
                }
            }
            ToDoEvents.HideDialog -> _showDialog.value = false
            ToDoEvents.ShowDialog -> _showDialog.value = true
            is ToDoEvents.DeleteTaskList -> {
                viewModelScope.launch {
                    Dao.deleteTasksList(event.tasks)
                }
            }

            is ToDoEvents.addListToChecklist -> _checkList.addAll(event.taskList)
            is ToDoEvents.addToCheckList -> _checkList.add(event.task)
            ToDoEvents.clearCheckList -> _checkList.clear()
            is ToDoEvents.removeFromCheckList -> _checkList.remove(event.task)
        }
    }

}