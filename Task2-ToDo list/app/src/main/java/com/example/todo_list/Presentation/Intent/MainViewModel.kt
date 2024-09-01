package com.example.todo_list.Presentation.Intent

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todo_list.Domain.Dao
import com.example.todo_list.Data.TaskInfo
import com.example.todo_list.Presentation.Model.ToDoStates
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class MainViewModel(
    val Dao: Dao
) : ViewModel() {


    private val _checkList = mutableStateListOf<TaskInfo>()
    val checkList : List<TaskInfo> = _checkList

    private val _state = MutableStateFlow(ToDoStates())
    val state = _state.asStateFlow()

    private val taskList = Dao.getAllTasks()

    private val _searchTxt = MutableStateFlow("")

    val Tasks = combine(taskList, _searchTxt) { list, txt ->
        list.filter { it.title.contains(txt, true) || it.description.contains(txt, true) }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        viewModelScope.launch {
            delay(1000)
            _state.update {
                it.copy(
                    appLoading = false
                )
            }
        }
    }


    fun onEvent(event: ToDoEvents) {
        when (event) {
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

            ToDoEvents.HideDialog -> _state.update { it.copy(showDialog = false) }
            ToDoEvents.ShowDialog -> _state.update { it.copy(showDialog = true) }
            is ToDoEvents.DeleteTaskList -> {
                viewModelScope.launch {
                    Dao.deleteTasksList(event.tasks)
                }
            }

            is ToDoEvents.addListToChecklist -> _checkList.addAll(event.taskList)
            is ToDoEvents.addToCheckList -> _checkList.add(event.task)
            ToDoEvents.clearCheckList -> _checkList.clear()
            is ToDoEvents.removeFromCheckList -> _checkList.remove(event.task)
            is ToDoEvents.searchFor -> {
                _searchTxt.value = event.text
            }

            is ToDoEvents.showEditDialog -> {
                _state.update { it.copy(editTask = event.task, showEditDialog = true) }
            }

            ToDoEvents.HideEditDialog -> _state.update { it.copy(showEditDialog = false) }
            is ToDoEvents.updateCheckList -> {
                _checkList[event.index] = event.newTask
            }
        }
    }

}