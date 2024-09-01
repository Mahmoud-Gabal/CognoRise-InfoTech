package com.example.todo_list.Presentation.View

import android.os.Build
import android.os.Build.VERSION.SDK_INT
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
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
import androidx.compose.material.icons.filled.Edit
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import com.example.todo_list.Data.TaskInfo
import com.example.todo_list.Presentation.Model.ToDoEvents
import com.example.todo_list.Presentation.Model.ToDoStates
import com.example.todo_list.R
import com.example.todo_list.ui.theme.TaskScreenColor


@RequiresApi(Build.VERSION_CODES.P)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class,
    ExperimentalAnimationGraphicsApi::class
)
@Preview(showBackground = true)
@Composable
fun TaskScreen(
    modifier: Modifier = Modifier,
    tasks: List<TaskInfo> = listOf(TaskInfo("1", "fff")),
    onEvent: (ToDoEvents) -> Unit = {},
    state: ToDoStates = ToDoStates(),
    checkList: List<TaskInfo> = emptyList()
) {
    if (state.showDialog) {
        taskDialog(onEvent = onEvent)
    }
    if (state.showEditDialog){
        EditDialog(editTask = state.editTask, onEvent = onEvent, checkedList = checkList)
    }
    var isVisible by rememberSaveable {
        mutableStateOf(false)
    }

//        Instead of making custom saver , another solution we made it in the viewModel

//    val checkedList = rememberSaveable {
//        mutableStateListOf<TaskInfo>()
//    }
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
                                    if (checkList.containsAll(tasks)) {
                                        onEvent(ToDoEvents.clearCheckList)
                                    } else {
                                        val remainingTasks = tasks.filter { !checkList.contains(it) }
                                        onEvent(ToDoEvents.addListToChecklist(remainingTasks))
                                    }
                                }) {
                                    Text(
                                        text = if (checkList.containsAll(tasks)) "Unselect all" else "Select all",
                                        color = Color.White,
                                        fontSize = 19.sp
                                    )
                                }
                            }
                            if (checkList.isNotEmpty()) {
                                IconButton(onClick = {
                                    onEvent(ToDoEvents.DeleteTaskList(checkList.toList()))
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(TaskScreenColor)
        ) {
            SearchBar(onEvent = onEvent)
            if (state.appLoading || tasks.isEmpty()){
                LoadingIcon(Modifier.weight(1f).fillMaxWidth())
            }else{
                LazyColumn(
                    modifier = modifier
                        .weight(1f)
                        .background(TaskScreenColor),
                    contentPadding = PaddingValues(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {

                    items(tasks) { task ->
                        val cardColor by animateColorAsState(
                            targetValue = if (checkList.contains(task)) Color.LightGray else Color.White,
                            animationSpec = tween(100), label = ""
                        )
                        val newHorizontalPadding by animateIntAsState(
                            targetValue = if (checkList.contains(task)) 10 else 0,
                            animationSpec = tween(100), label = ""
                        )
                        val newVerticalPadding by animateIntAsState(
                            targetValue = if (checkList.contains(task)) 4 else 0,
                            animationSpec = tween(100), label = ""
                        )
                        Row(
                            modifier = Modifier
                                .padding(newHorizontalPadding.dp, vertical = newVerticalPadding.dp)
                                .fillMaxWidth()
                                .defaultMinSize(minHeight = 70.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(cardColor)
                                .combinedClickable(
                                    onLongClick = {
                                        if (checkList.isEmpty()) {
                                            onEvent(ToDoEvents.addToCheckList(task))
                                            isVisible = true
                                        } else {
                                            if (checkList.contains(task)) {
                                                onEvent(ToDoEvents.removeFromCheckList(task))
                                            } else {
                                                onEvent(ToDoEvents.addToCheckList(task))
                                            }
                                        }
                                    }
                                ) {
                                    if (isVisible) {
                                        if (checkList.contains(task)) {
                                            onEvent(ToDoEvents.removeFromCheckList(task))
                                        } else {
                                            onEvent(ToDoEvents.addToCheckList(task))
                                        }
                                    }
                                }
                            ,
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(
                                verticalArrangement = Arrangement.spacedBy(6.dp),
                                modifier = Modifier
                                    .fillMaxWidth(.77f)
                                    .padding(start = 8.dp, top = 8.dp, bottom = 8.dp)
                            ) {
                                Text(
                                    text = task.title,
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.Black
                                )
                                Text(
                                    text = task.description,
                                    fontSize = 18.sp,
                                    color = Color.Black
                                )
                            }
                            Row{
                                AnimatedVisibility(visible = isVisible) {
                                    Checkbox(
                                        checked = checkList.contains(task),
                                        onCheckedChange = {
                                            if (it) {
                                                onEvent(ToDoEvents.addToCheckList(task))
                                            } else {
                                                onEvent(ToDoEvents.removeFromCheckList(task))
                                            }
                                        }
                                    )
                                }
                                IconButton(onClick = {
                                    onEvent(ToDoEvents.showEditDialog(task))
                                }) {
                                    Icon(
                                        imageVector = Icons.Filled.Edit,
                                        contentDescription = null,
                                        tint = Color.Black
                                    )
                                }

                            }

                        }

                    }

                }
            }
        }


    }


}

@Composable
fun LoadingIcon(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    )  {
        val ImageLoader = ImageLoader.Builder(LocalContext.current)
            .components {
                if (SDK_INT >= 28) {
                    add(ImageDecoderDecoder.Factory())
                } else {
                    add(GifDecoder.Factory())
                }
            }
            .build()
        val ImageRequest = ImageRequest.Builder(LocalContext.current)
            .data(R.drawable.icon)
            .build()
        Image(
            painter = rememberAsyncImagePainter(model = ImageRequest, imageLoader = ImageLoader),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(130.dp),
        )
    }
}