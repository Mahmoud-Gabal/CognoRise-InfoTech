package com.example.todo_list.Presentation.View

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.example.todo_list.Data.TaskInfo
import com.example.todo_list.Presentation.Intent.ToDoEvents
import com.example.todo_list.R
import com.example.todo_list.ui.theme.TaskScreenColor

@Composable
fun taskDialog(
    modifier: Modifier = Modifier,
    onEvent: (ToDoEvents) -> Unit,
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
//    we made custom saver for TaskInfo
    var task by rememberSaveable(saver = CustomSaver.taskSaver) {
        mutableStateOf(TaskInfo("", ""))
    }
    var isTitleError by rememberSaveable {
        mutableStateOf(false)
    }
    var isDescriptionError by rememberSaveable {
        mutableStateOf(false)
    }
    AlertDialog(
        onDismissRequest = { onEvent(ToDoEvents.HideDialog) },
        confirmButton = {
            Button(
                onClick = {

                    if (task.title.isNotEmpty() && task.description.isNotEmpty()) {
                        isTitleError = false
                        isDescriptionError = false
                        onEvent(ToDoEvents.AddTask(task))
                        onEvent(ToDoEvents.HideDialog)
                    } else if (task.title.isEmpty() && task.description.isEmpty()) {
                        isTitleError = true
                        isDescriptionError = true
                    }else if (task.title.isEmpty()){
                        isTitleError = true
                        isDescriptionError = false
                    }else{
                        isDescriptionError = true
                        isTitleError = false
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
                    isError = isTitleError&&task.title.isEmpty(),
                    trailingIcon = {
                        if (isTitleError&&task.title.isEmpty())
                            Icon(Icons.Filled.Warning,"error", tint =Color.Red)
                    },
                    supportingText = {
                        if (isTitleError&&task.title.isEmpty()){
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = "Title can't be empty !",
                                color = Color.Red
                            )
                        }
                    },
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
                        unfocusedIndicatorColor = Color.Transparent,
                        errorContainerColor =Color.White ,
                        errorIndicatorColor = Color.Red,
                        errorCursorColor = Color.Red,
                        errorTextColor = Color.Black,
                        errorPlaceholderColor = Color.Gray
                    ),
                    shape = RoundedCornerShape(15.dp),
                    maxLines = 2,
                    modifier = Modifier.verticalScroll(scrollState)
                )
                TextField(
                    isError = isDescriptionError&&task.description.isEmpty(),
                    trailingIcon = {
                        if (isDescriptionError&&task.description.isEmpty())
                            Icon(Icons.Filled.Warning,"error", tint = Color.Red)
                    },
                    supportingText = {
                        if (isDescriptionError&&task.description.isEmpty()){
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = "Description can't be empty !",
                                color = Color.Red
                            )
                        }
                    },
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
                        unfocusedIndicatorColor = Color.Transparent,
                        errorContainerColor =Color.White ,
                        errorIndicatorColor = Color.Red,
                        errorCursorColor = Color.Red,
                        errorTextColor = Color.Black,
                        errorPlaceholderColor = Color.Gray
                    ),
                    shape = RoundedCornerShape(15.dp),
                    maxLines = 3,
                    modifier = Modifier.verticalScroll(scrollState)
                )
            }
        },
        containerColor = TaskScreenColor,
        textContentColor = Color.White

    )

}


