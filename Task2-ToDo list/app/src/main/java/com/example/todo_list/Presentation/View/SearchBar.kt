package com.example.todo_list.Presentation.View

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.todo_list.Presentation.Intent.ToDoEvents
import com.example.todo_list.R

@Preview(showBackground = true)
@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    onEvent : (ToDoEvents) -> Unit = {}
) {
    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()
    var task by rememberSaveable {
        mutableStateOf("")
    }
    TextField(
        value = task,
        onValueChange = {
            task = it
            onEvent(ToDoEvents.searchFor(task))
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = {
            onEvent(ToDoEvents.searchFor(task))
            focusManager.clearFocus()
        }

        )
        ,
        modifier = modifier
            .fillMaxWidth()
//            .height(73.dp)
            .padding(start = 6.dp, end = 6.dp, top = 6.dp, bottom = 6.dp)
            .clip(RoundedCornerShape(20.dp))
            .verticalScroll(scrollState)
        ,
        placeholder = { Text(text = "Search",color = Color.White)},
        colors = TextFieldDefaults.colors(
            focusedContainerColor = colorResource(id = R.color.lightBlue),
            unfocusedContainerColor = colorResource(id = R.color.lightBlue),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            cursorColor = Color.White,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White
        ),
        maxLines = 3,
        leadingIcon = {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.padding(start = 5.dp)

            )
        }
    )

}