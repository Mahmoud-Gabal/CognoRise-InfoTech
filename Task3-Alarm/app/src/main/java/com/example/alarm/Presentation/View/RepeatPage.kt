package com.example.alarm.Presentation.View

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.alarm.Presentation.Intent.SetAlarmEvents
import com.example.alarm.Presentation.View.Constants.daysOfWeek
import com.example.alarm.Presentation.View.NavGraph.Routes
import com.example.alarm.R

@OptIn(ExperimentalMaterial3Api::class)
//@Preview(showBackground = true)
@Composable
fun RepeatPage(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    customDays: List<String>,
    onSetAction : (SetAlarmEvents) -> Unit
) {

    Scaffold(
        containerColor = colorResource(R.color.homeBackground),
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigate(Routes.SetAlarm.route)
                    }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = null,
                            modifier = Modifier.size(30.dp),
                            tint = Color.DarkGray
                        )
                    }
                },
                actions = {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Add alarm",
                            color = Color.Black,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorResource(R.color.homeBackground)
                )
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            val isChecked =
                if (customDays.isEmpty()) {
                    0
                } else if (customDays.containsAll(daysOfWeek)) {
                    1
                } else {
                    2
                }
            val choices = listOf("Once", "Daily", "Custom")
            var showPopUp by rememberSaveable {
                mutableStateOf(false)
            }
            if (showPopUp) {
                CustomPopUp(
                    customDays = customDays,
                    onConfirm = { updatedDays ->
                        if (updatedDays.isEmpty()) {
                            onSetAction(SetAlarmEvents.ResetDays)
                            showPopUp = false
                        } else if (updatedDays.containsAll(daysOfWeek)) {
                            onSetAction(SetAlarmEvents.ResetDays)
                            onSetAction(SetAlarmEvents.AddDaysList(updatedDays.toList()))
                            showPopUp = false
                        } else {
                            onSetAction(SetAlarmEvents.ResetDays)
                            onSetAction(SetAlarmEvents.AddDaysList(updatedDays.toList()))
                            showPopUp = false
                        }
                    },
                    onDismiss = {
                        showPopUp = false
                    }
                )
            }
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                itemsIndexed(choices) { index, item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(
                                if (isChecked == index) colorResource(R.color.lightBlueTR) else colorResource(
                                    R.color.lightGray
                                )
                            )
                            .clickable {
                                if (index == 2) showPopUp = true
                                else if (index == 1) {
                                    onSetAction(SetAlarmEvents.ResetDays)
                                    onSetAction(SetAlarmEvents.AddDaysList(daysOfWeek))
                                } else {
                                    onSetAction(SetAlarmEvents.ResetDays)
                                }
                            }
                            .padding(horizontal =10.dp)
                        ,
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row{
                            Icon(
                                imageVector = Icons.Filled.Check,
                                contentDescription = null,
                                tint = if (isChecked == index) colorResource(R.color.lightBlue) else Color.Transparent
                            )

                            Text(
                                text = item,
                                fontWeight = FontWeight.Medium,
                                fontSize = 18.sp,
                                modifier = Modifier.padding(horizontal = 4.dp),
                                color = if (isChecked == index) colorResource(R.color.lightBlue) else Color.Unspecified
                            )
                        }
                        if (index==2){
                            Icon(
                                imageVector = Icons.Filled.KeyboardArrowRight,
                                contentDescription = null,
                                tint = Color.Gray,
                                modifier = Modifier.size(25.dp).clip(CircleShape).background(Color.LightGray)
                            )
                        }
                    }
                }
            }
        }
    }

}


@Composable
fun CustomPopUp(
    modifier: Modifier = Modifier,
    customDays: List<String>,
    onConfirm: (SnapshotStateList<String>) -> Unit,
    onDismiss: () -> Unit
) {

    Popup(
        onDismissRequest = onDismiss
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(Color.White)
        ) {
            Text(
                text = "Customise",
                color = Color.Black,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp),
                textAlign = TextAlign.Center
            )

            val updatedDays = rememberSaveable(saver = Constants.saver) {
                customDays.toMutableStateList()
            }
            for (day in daysOfWeek) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .background(
                            if (updatedDays.contains(day)) colorResource(R.color.lightGray) else Color.Unspecified
                        )
                        .clickable {
                            if (updatedDays.contains(day)) {
                                updatedDays.remove(day)
                            } else {
                                updatedDays.add(day)
                            }
                        }
                        .padding(horizontal = 15.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = day,
                        fontWeight = FontWeight.Medium,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(horizontal = 8.dp),
                    )
                    Checkbox(
                        checked = updatedDays.contains(day),
                        onCheckedChange = {
                            if (it) {
                                updatedDays.add(day)
                            } else {
                                updatedDays.remove(day)
                            }
                        }
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp, vertical = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(R.color.homeBackground)
                    ),
                    modifier = Modifier
                        .width(165.dp)
                        .height(50.dp)
                ) {
                    Text(
                        text = "Cancel",
                        fontSize = 18.sp,
                        color = Color.Black
                    )
                }
                Button(
                    onClick = {
                        onConfirm(updatedDays)
                    },
                    modifier = Modifier
                        .width(165.dp)
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(R.color.lightBlue)
                    )
                ) {
                    Text(
                        text = "Ok",
                        fontSize = 18.sp,
                        color = Color.White
                    )
                }
            }
        }
    }

}