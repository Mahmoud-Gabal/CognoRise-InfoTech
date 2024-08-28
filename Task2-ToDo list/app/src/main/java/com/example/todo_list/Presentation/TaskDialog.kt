package com.example.todo_list.Presentation

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.SaverScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.example.todo_list.Domain.Db.TaskInfo
import com.example.todo_list.Domain.ToDoEvents
import com.example.todo_list.R
import com.example.todo_list.ui.theme.TaskScreenColor

@Composable
fun taskDialog(
    modifier: Modifier = Modifier,
    onEvent: (ToDoEvents) -> Unit
) {
    val context = LocalContext.current
//    we made custom saver for TaskInfo
    var task by rememberSaveable(saver = CustomSaver.taskSaver) {
        mutableStateOf(TaskInfo("", ""))
    }
    AlertDialog(
        onDismissRequest = { onEvent(ToDoEvents.HideDialog) },
        confirmButton = {
            Button(
                onClick = {

                    if (task.title.isNotEmpty() && task.description.isNotEmpty()) {
                        onEvent(ToDoEvents.AddTask(task))
                        onEvent(ToDoEvents.HideDialog)
                    } else {
                        Toast.makeText(context, "Empty places not allowed !", Toast.LENGTH_SHORT).show()
                    }

                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.lightBlue)
                )
            ) {
                Text(text = "Add", color = Color.White)
            }
        },
        dismissButton = {
            Button(
                onClick = {
                    onEvent(ToDoEvents.HideDialog)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.lightBlue)
                )
            ) {
                Text(text = "Cancel", color = Color.White)
            }

        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TextField(
                    value = task.title,
                    onValueChange = { task = task.copy(title = it) },
                    placeholder = { Text(text = "title") },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedPlaceholderColor = Color.Gray,
                        unfocusedPlaceholderColor = Color.Gray,
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black,
                        focusedIndicatorColor = colorResource(id = R.color.lightBlue),
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    shape = RoundedCornerShape(15.dp)
                )
                TextField(
                    value = task.description,
                    onValueChange = { task = task.copy(description = it) },
                    placeholder = { Text(text = "description") },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedPlaceholderColor = Color.Gray,
                        unfocusedPlaceholderColor = Color.Gray,
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black,
                        focusedIndicatorColor = colorResource(id = R.color.lightBlue),
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    shape = RoundedCornerShape(15.dp)
                )
            }
        },
        containerColor = TaskScreenColor,
        textContentColor = Color.White

    )

}

object CustomSaver{
    val taskSaver = object : Saver<MutableState<TaskInfo>,List<Any>>{
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

