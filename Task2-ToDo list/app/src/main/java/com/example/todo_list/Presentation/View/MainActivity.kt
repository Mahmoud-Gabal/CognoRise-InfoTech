package com.example.todo_list.Presentation.View

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.todo_list.Data.TaskDb
import com.example.todo_list.Presentation.Intent.MainViewModel
import com.example.todo_list.ui.theme.TaskScreenColor
import com.example.todo_list.ui.theme.ToDoListTheme

@Suppress("UNCHECKED_CAST")
class MainActivity : ComponentActivity() {
    private val taskDb by lazy {
        Room.databaseBuilder(
            applicationContext,
            TaskDb::class.java,
            "task-db"
        ).build()
    }
    private val mainViewModel by viewModels<MainViewModel>(
        factoryProducer ={
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return MainViewModel(taskDb.Dao) as T
                }
            }
        }

    )
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            navigationBarStyle = SystemBarStyle.light(
                TaskScreenColor.toArgb(),
                TaskScreenColor.toArgb()
            )
        )
        setContent { 
            ToDoListTheme {
                val taskList = mainViewModel.Tasks.collectAsState().value
                val state = mainViewModel.state.collectAsState().value
                val checkList = mainViewModel.checkList
                TaskScreen(
                    tasks = taskList,
                    onEvent = mainViewModel::onEvent,
                    state = state,
                    checkList = checkList
                )
            }
        }
    }
}