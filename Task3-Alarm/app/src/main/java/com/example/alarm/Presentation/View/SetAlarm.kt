@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.alarm.Presentation.View

import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.alarm.Data.AlarmData
import com.example.alarm.Presentation.Intent.AlarmEvents
import com.example.alarm.Presentation.Intent.SetAlarmEvents
import com.example.alarm.Presentation.View.NavGraph.Routes
import com.example.alarm.R
import kotlinx.coroutines.delay
import java.time.Clock
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.util.Calendar
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
//@Preview(showBackground = true)
@Composable
fun SetAlarm(
    modifier: Modifier = Modifier,
    navController: NavController = rememberNavController(),
    onEvent: (AlarmEvents) -> Unit = {},
    customDays: List<String> = emptyList(),
    alarmData: AlarmData = AlarmData(),
    onSetAction: (SetAlarmEvents) -> Unit = {},
    oldAlarm: AlarmData
) {
    val context = LocalContext.current
    Scaffold(
        containerColor = colorResource(R.color.homeBackground),
        topBar = {
            TopAppBar(
                title = {},
                actions = {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        IconButton(onClick = {
                            navController.navigate(Routes.HomeScreen.route)
                        }) {
                            Icon(
                                Icons.Filled.Clear,
                                null,
                                modifier = Modifier.size(30.dp),
                                tint = colorResource(R.color.titleText)
                            )
                        }
                        Text(text = "Add alarm", color = colorResource(R.color.titleText), fontSize = 20.sp)
                        IconButton(onClick = {

//                         for edit/add do this :
//                             1/ remove + stop old alarm
                            onSetAction(SetAlarmEvents.StopAlarm(oldAlarm))
                            onEvent(AlarmEvents.RemoveAlarm(oldAlarm))

//                            2/ add another alarm + schedule it (for edit + add)
                            onEvent(AlarmEvents.AddAlarm(alarmData))
                            if (oldAlarm.status == true) {
                                onSetAction(SetAlarmEvents.ScheduleAlarm(alarmData))
                            }
                            if (alarmData.newTimeMillis == -2L) {
                                Toast.makeText(
                                    context,
                                    "Alarm will ring tomorrow at ${alarmData.hour}:${alarmData.minute} ${alarmData.additionalInfo}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                Toast.makeText(
                                    context,
                                    "Alarm will ring today at ${alarmData.hour}:${alarmData.minute} ${alarmData.additionalInfo}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            Log.d("alarmScheduled", "alarm added : $alarmData")
//                            3/ navigate to home screen
                            navController.navigate(Routes.HomeScreen.route)


                        }) {
                            Icon(
                                Icons.Filled.Check,
                                null,
                                modifier = Modifier.size(30.dp),
                                tint = colorResource(R.color.titleText)
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorResource(R.color.homeBackground)
                )
            )
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            contentPadding = PaddingValues(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            item {

                var currentTime by rememberSaveable { mutableStateOf(LocalDateTime.now()) }
                LaunchedEffect(currentTime) {
                    delay(1000)
                    currentTime = LocalDateTime.now()
                }
                val pickerState = rememberTimePickerState(
                    initialHour =
                    if (alarmData.timeInMillis == 0.toLong())
                        currentTime.hour
                    else Date(alarmData.timeInMillis).hours,
                    initialMinute =
                    if (alarmData.timeInMillis == 0.toLong())
                        currentTime.minute
                    else Date(alarmData.timeInMillis).minutes,
                    is24Hour = false
                )

                val calender = Calendar.getInstance().apply {
                    timeInMillis = System.currentTimeMillis()
                    set(Calendar.HOUR_OF_DAY, pickerState.hour)
                    set(Calendar.MINUTE, pickerState.minute)
                    set(Calendar.SECOND, 0)
                }
//                this check to make alarm tomorrow if our current time passed alarm defined
                if (currentTime.atZone(ZoneId.systemDefault())
                        .toEpochSecond() * 1000 >= calender.timeInMillis
                ) {
                    onSetAction(SetAlarmEvents.AddTime(calender.timeInMillis.plus(24 * 60 * 60 * 1000L)))
                    onSetAction(SetAlarmEvents.AddNewTimeMillis(-2L))
                } else {
                    onSetAction(SetAlarmEvents.AddTime(calender.timeInMillis))
                    onSetAction(SetAlarmEvents.AddNewTimeMillis(-1L))
                }
                onSetAction(SetAlarmEvents.AddHourMultiplyMinute(pickerState.hour * pickerState.minute))
                onSetAction(SetAlarmEvents.AddHour(pickerState.hour))
                onSetAction(SetAlarmEvents.AddMinute(pickerState.minute))
                if (pickerState.hour <= 12) {
                    onSetAction(SetAlarmEvents.AddAdditionalInfo("Am"))
                    onSetAction(SetAlarmEvents.AddTimeInText("${pickerState.hour}:${pickerState.minute} Am"))
                } else {
                    onSetAction(SetAlarmEvents.AddAdditionalInfo("Pm"))
                    onSetAction(SetAlarmEvents.AddTimeInText("${pickerState.hour}:${pickerState.minute} Pm"))
                }
                TimePicker(
                    state = pickerState,
                    modifier = Modifier.padding(vertical = 25.dp),
                    colors = TimePickerDefaults.colors(
                        clockDialColor = colorResource(R.color.alarmBackground),
                        selectorColor = Color.LightGray,
                        clockDialSelectedContentColor = Color.Black,
                        periodSelectorUnselectedContainerColor = colorResource(R.color.unSelectedPeriod),
                        periodSelectorSelectedContainerColor = colorResource(R.color.selectedPeriod),
                        periodSelectorSelectedContentColor = colorResource(R.color.periodSelectorSelectedContentColor),
                        periodSelectorUnselectedContentColor = colorResource(R.color.periodSelectorUnselectedContentColor),
                        timeSelectorUnselectedContainerColor = colorResource(R.color.unSelectedPeriod),
                        timeSelectorSelectedContainerColor = colorResource(R.color.selectedPeriod),
                        timeSelectorUnselectedContentColor = colorResource(R.color.timeSelectorUnselectedContentColor),
                        timeSelectorSelectedContentColor = colorResource(R.color.timeSelectorSelectedContentColor)
                    )
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color.Transparent)
                        .clickable {
                            navController.navigate(Routes.RingTonePage.route)
                        }
                        .padding(horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Ringtone",
                        fontWeight = FontWeight.Medium,
                        fontSize = 18.sp,
                        modifier = Modifier,
                        color = colorResource(R.color.titleText)
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text =
                            if (alarmData.ringTone == R.raw.night) "Night"
                            else if (alarmData.ringTone == R.raw.funny) "Funny"
                            else if (alarmData.ringTone == R.raw.drama) "Drama"
                            else "Dream",
                            color = Color.Gray
                        )
                        Icon(
                            imageVector = Icons.Filled.KeyboardArrowRight,
                            contentDescription = null,
                            tint = Color.Gray,
                            modifier = Modifier.size(25.dp)
                        )
                    }
                }
                var showDialog by rememberSaveable {
                    mutableStateOf(false)
                }

                if (showDialog) {
                    DescriptionDialog(
                        onDismiss = { showDialog = false },
                        onConfirm = {
                            onSetAction(SetAlarmEvents.AddDescription(it))
                        },
                        description = alarmData.description
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .defaultMinSize(minHeight = 60.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(color = colorResource(R.color.cardBackground))
                        .clickable {
                            showDialog = true
                        }
                        .padding(horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Description",
                        fontWeight = FontWeight.Medium,
                        fontSize = 18.sp,
                        modifier = Modifier,
                        color = colorResource(R.color.titleText)
                    )
                    Text(
                        text =
                        if (alarmData.description.isEmpty()) "Enter description"
                        else if (alarmData.description.length <= 47) alarmData.description
                        else alarmData.description.slice(0..46) + "...",
                        color = Color.Gray,
                        modifier = Modifier.padding(10.dp),
                        maxLines = 2
                    )


                }
            }

        }
    }

}

@Preview(showBackground = true)
@Composable
fun DescriptionDialog(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit = {},
    onConfirm: (String) -> Unit = {},
    description: String = ""
) {
    var newDescription by rememberSaveable {
        mutableStateOf(description)
    }
    AlertDialog(
        onDismissRequest = onDismiss,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(colorResource(R.color.alarmBackground))
                .padding(horizontal = 20.dp)
        ) {
            Text(
                text = "Add alarm description",
                fontSize = 18.sp,
                color = colorResource(R.color.titleText),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp),
                textAlign = TextAlign.Center
            )
            TextField(
                value = newDescription,
                onValueChange = { newDescription = it },
                maxLines = 3,
                modifier = Modifier
                    .border(
                        width = 2.dp,
                        color = colorResource(R.color.lightBlue),
                        shape = RoundedCornerShape(15.dp)
                    )
                    .verticalScroll(state = rememberScrollState()),
                placeholder = { Text(text = "Enter description") },
                shape = RoundedCornerShape(15.dp),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = colorResource(R.color.lightBlue),
                    focusedPlaceholderColor = Color.LightGray,
                    unfocusedPlaceholderColor = Color.LightGray,
                    focusedContainerColor = colorResource(R.color.alarmBackground),
                    unfocusedContainerColor = colorResource(R.color.alarmBackground),
                )
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(R.color.cancelButtonContainer)
                    ),
                    modifier = Modifier
                        .width(135.dp)
                        .height(50.dp)
                ) {
                    Text(
                        text = "Cancel",
                        fontSize = 18.sp,
                        color = colorResource(R.color.cancelButtonContent)
                    )
                }
                Button(
                    onClick = {
                        onConfirm(newDescription)
                        onDismiss()
                    },
                    modifier = Modifier
                        .width(135.dp)
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(R.color.lightBlue)
                    )
                ) {
                    Text(
                        text = "Set",
                        fontSize = 18.sp,
                        color = Color.White
                    )
                }
            }
        }
    }

}
