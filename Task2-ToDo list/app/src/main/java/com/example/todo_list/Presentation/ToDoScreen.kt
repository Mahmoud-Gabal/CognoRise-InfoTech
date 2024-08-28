package com.example.todo_list.Presentation

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todo_list.Domain.Db.TaskInfo
import com.example.todo_list.Domain.ToDoEvents
import com.example.todo_list.R
import com.example.todo_list.ui.theme.TaskScreenColor


@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun TaskScreen(
    modifier: Modifier = Modifier,
    tasks: List<TaskInfo> = emptyList(),
    showDialog: Boolean = false,
    onEvent: (ToDoEvents) -> Unit = {},
    checkedList: List<TaskInfo> = emptyList()
) {
    if (showDialog) {
        taskDialog(onEvent = onEvent)
    }
    var isVisible by rememberSaveable {
        mutableStateOf(false)
    }

//        Instead of making custom saver , another solution we made it in the viewModel

//    val checkedList = rememberSaveable {
//        mutableStateListOf<TaskInfo>()
//    }

    Log.d("checkListNow", "checkList now is : ${checkedList.map { it.title }}")
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                actions = {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp)
                    ) {
                        Text(
                            text = "TODo",
                            fontSize = 27.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 10.dp)
                        )
                        Row(
                            modifier = Modifier.fillMaxHeight(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            if (isVisible) {
                                IconButton(onClick = {
                                    isVisible = false
                                    onEvent(ToDoEvents.clearCheckList)
                                }) {
                                    Icon(
                                        imageVector = Icons.Filled.Clear,
                                        contentDescription = null,
                                        modifier = Modifier.size(30.dp),
                                        tint = Color.White
                                    )
                                }
                                TextButton(onClick = {
                                    if (checkedList.containsAll(tasks)) {
                                        onEvent(ToDoEvents.clearCheckList)
                                    } else {
                                        val remainingTasks = tasks.filter { !checkedList.contains(it) }
                                        onEvent(ToDoEvents.addListToChecklist(remainingTasks))
                                    }
                                }) {
                                    Text(
                                        text = if (checkedList.containsAll(tasks)) "Unselect all" else "Select all",
                                        color = Color.White,
                                        fontSize = 19.sp
                                    )
                                }
                            }
                            if (checkedList.isNotEmpty()) {
                                IconButton(onClick = {
                                    onEvent(ToDoEvents.DeleteTaskList(checkedList.toList()))
                                    onEvent(ToDoEvents.clearCheckList)
                                    isVisible = false
                                }) {
                                    Icon(
                                        imageVector = Icons.Filled.Delete,
                                        contentDescription = null,
                                        modifier = Modifier.size(30.dp),
                                        tint = Color.White
                                    )
                                }
                            }
                            IconButton(onClick = {
                                onEvent(ToDoEvents.ShowDialog)
                            }) {
                                Icon(
                                    imageVector = Icons.Filled.Add,
                                    contentDescription = null,
                                    modifier = Modifier.size(30.dp),
                                    tint = Color.White
                                )
                            }
                        }
                    }
                },
                title = {},
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorResource(id = R.color.lightBlue)
                ),

                )
        },

        ) { innerPadding ->
        LazyColumn(
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(TaskScreenColor),
            contentPadding = PaddingValues(6.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            items(tasks) { task ->
                val cardColor by animateColorAsState(
                    targetValue = if (checkedList.contains(task)) Color.LightGray else Color.White,
                    animationSpec = tween(100), label = ""
                )
                val newHorizontalPadding by animateIntAsState(
                    targetValue = if (checkedList.contains(task)) 10 else 0,
                    animationSpec = tween(100), label = ""
                )
                val newVerticalPadding by animateIntAsState(
                    targetValue = if (checkedList.contains(task)) 5 else 0,
                    animationSpec = tween(100), label = ""
                )
                Row(
                    modifier = Modifier
                        .padding(newHorizontalPadding.dp, vertical = newVerticalPadding.dp)
                        .fillMaxWidth()
                        .defaultMinSize(minHeight = 70.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(cardColor)
//                            you must give tasks as key to make Gestures recognizer update state when tasks change
                        .pointerInput(tasks) {
                            detectTapGestures(
                                onLongPress = {
                                    if (checkedList.isEmpty()) {
                                        onEvent(ToDoEvents.addToCheckList(task))
                                        isVisible = true
                                    } else {
                                        if (checkedList.contains(task)) {
                                            onEvent(ToDoEvents.removeFromCheckList(task))
                                        } else {
                                            onEvent(ToDoEvents.addToCheckList(task))
                                        }
                                    }
                                },
                                onTap = {
                                    if (isVisible) {
                                        if (checkedList.contains(task)) {
                                            onEvent(ToDoEvents.removeFromCheckList(task))
                                        } else {
                                            onEvent(ToDoEvents.addToCheckList(task))
                                        }
                                    }
                                }
                            )
                        }
                    ,
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier
                            .fillMaxWidth(.9f)
                            .padding(horizontal = 8.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = task.title,
                            fontSize = 23.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.Black
                        )
                        Text(
                            text = task.description,
                            fontSize = 19.sp,
                            color = Color.Black
                        )
                    }
                    AnimatedVisibility(visible = isVisible) {
                        Checkbox(
                            checked = checkedList.contains(task),
                            onCheckedChange = {
                                if (it) {
                                    onEvent(ToDoEvents.addToCheckList(task))
                                } else {
                                    if (checkedList.contains(task) && checkedList.size == 1) {
                                        onEvent(ToDoEvents.removeFromCheckList(task))
                                    } else {
                                        onEvent(ToDoEvents.removeFromCheckList(task))
                                    }
                                }
                            }
                        )
                    }


                }

            }

        }
    }


}