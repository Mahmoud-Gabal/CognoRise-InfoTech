package com.example.todo_list.Presentation.Model

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.SaverScope
import com.example.todo_list.Data.TaskInfo

object CustomSaver {
    val taskSaver = object : Saver<MutableState<TaskInfo>, List<Any>> {
        override fun restore(value: List<Any>): MutableState<TaskInfo>? {
            return mutableStateOf(
                TaskInfo(title = value[0].toString(), description = value[1].toString() )
            )
        }

        override fun SaverScope.save(value: MutableState<TaskInfo>): List<Any>? {
            return listOf(value.value.title,value.value.description)
        }
    }
}