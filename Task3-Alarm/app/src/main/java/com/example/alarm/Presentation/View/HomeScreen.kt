@file:OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class,
    DelicateCoroutinesApi::class
)

package com.example.alarm.Presentation.View

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconToggleButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButtonColors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.alarm.Data.AlarmData
import com.example.alarm.Presentation.Intent.AlarmEvents
import com.example.alarm.Presentation.Intent.SetAlarmEvents
import com.example.alarm.Presentation.View.NavGraph.Routes
import com.example.alarm.R
import com.google.gson.Gson
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Timer
import java.util.TimerTask

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("SimpleDateFormat", "QueryPermissionsNeeded")
@OptIn(ExperimentalMaterial3Api::class)
//@Preview(showBackground = true)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    alarms: List<AlarmData> = emptyList(),
    onEvent: (AlarmEvents) -> Unit = {},
    navController: NavHostController = rememberNavController(),
    onSetAction: (SetAlarmEvents) -> Unit
) {
//    to get local date and time and format them
    var dateTime by rememberSaveable { mutableStateOf(LocalDateTime.now()) }
//   we use launched effect to update our date-time every second
    LaunchedEffect(dateTime) {
        delay(1000)
        dateTime = LocalDateTime.now()
    }

    var isVisible by rememberSaveable {
        mutableStateOf(false)
    }
    val scope = rememberCoroutineScope()
//        we make a custom saver for mutableStateList

    val checkedList = rememberSaveable(saver = Constants.alarmDataSaver) {
        mutableStateListOf<AlarmData>()
    }
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = colorResource(R.color.homeBackground),
        topBar = {
            TopAppBar(
                title = {
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorResource(R.color.homeBackground)
                ),
                actions = {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp)
                    ) {
                        Text(
                            text = "Alarm",
                            fontSize = 30.sp,
                            color = colorResource(R.color.titleText),
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
                                    checkedList.clear()
                                }) {
                                    Icon(
                                        imageVector = Icons.Filled.Clear,
                                        contentDescription = null,
                                        modifier = Modifier.size(30.dp),
                                        tint = colorResource(R.color.titleText)
                                    )
                                }
                                TextButton(onClick = {
                                    if (checkedList.containsAll(alarms)) {
                                        checkedList.clear()
                                    } else {
                                        val remainingAlarms =
                                            alarms.filter { !checkedList.contains(it) }
                                        checkedList.addAll(remainingAlarms)
                                    }
                                }) {
                                    Text(
                                        text = if (checkedList.containsAll(alarms)) "Unselect all" else "Select all",
                                        color = colorResource(R.color.titleText),
                                        fontSize = 19.sp
                                    )
                                }
                            }
                            if (checkedList.isNotEmpty()) {
                                IconButton(onClick = {
                                    onEvent(AlarmEvents.RemoveAlarmsList(checkedList.toList()))
                                    val scheduledAlarms = checkedList.filter { it.status == true }
                                    for (scheduledAlarm in scheduledAlarms) {
                                        if (scheduledAlarm.newTimeMillis == -1L || scheduledAlarm.newTimeMillis == -2L) {
                                            onSetAction(
                                                SetAlarmEvents.StopAlarm(scheduledAlarm)
                                            )

                                        } else {
                                            onSetAction(
                                                SetAlarmEvents.StopAlarm(scheduledAlarm.copy(timeInMillis = scheduledAlarm.newTimeMillis))
                                            )
                                        }
                                    }
                                    checkedList.clear()
                                    isVisible = false
                                }) {
                                    Icon(
                                        imageVector = Icons.Filled.Delete,
                                        contentDescription = null,
                                        modifier = Modifier.size(30.dp),
                                        tint = colorResource(R.color.titleText)
                                    )
                                }
                            }
                        }
                    }
                },
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier
                    .offset(10.dp, -33.dp)
                    .size(55.dp)
                    .shadow(3.dp, ambientColor = Color.Gray, shape = CircleShape)
                    .clip(CircleShape),
                onClick = {
//                    reset all states for new alarm
                    onSetAction(SetAlarmEvents.ResetDays)
                    onSetAction(SetAlarmEvents.ResetAlarmData)
                    onSetAction(SetAlarmEvents.ResetOldAlarm)

                    navController.navigate(Routes.SetAlarm.route)
                },
                containerColor = colorResource(R.color.floatingButtonColor),
                contentColor = colorResource(R.color.lightBlue)
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = null,
                    modifier = Modifier.size(40.dp)
                )
            }
        }

    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
//
                val formatedDate = rememberSaveable(dateTime) {
                    DateTimeFormatter.ofPattern("EEE, d MMM yyyy").format(dateTime)
                }
                val formatedTime = rememberSaveable(dateTime) {
                    DateTimeFormatter.ofPattern("h:mm a").format(dateTime)
                }

                Text(
                    text = formatedTime,
                    fontSize = 55.sp,
                    color = colorResource(R.color.currentTime),
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = formatedDate,
                    fontSize = 23.sp,
                    color = colorResource(R.color.currentDate)
                )

            }
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(
                    start = 10.dp,
                    end = 10.dp,
                    top = 13.dp,
                    bottom = 110.dp
                ),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                items(alarms) { alarm ->
                    var isScheduled by rememberSaveable { mutableStateOf(false) }
                    val pickerState = rememberTimePickerState(
                        initialHour =
                        Date(alarm.timeInMillis).hours,
                        initialMinute =
                        Date(alarm.timeInMillis).minutes,
                        is24Hour = false
                    )
                    val calender = Calendar.getInstance().apply {
                        timeInMillis = System.currentTimeMillis()
                        set(Calendar.HOUR_OF_DAY, pickerState.hour)
                        set(Calendar.MINUTE, pickerState.minute)
                        set(Calendar.SECOND, 0)
                    }

//                this check to make alarm tomorrow if our current time passed alarm defined
                    val newTimeInMillis = if (dateTime.atZone(ZoneId.systemDefault())
                            .toEpochSecond() * 1000 >= calender.timeInMillis
                    ) {
                        calender.timeInMillis.plus( 24 * 60 * 60 * 1000L)
                    } else {
                        calender.timeInMillis
                    }

                    val context = LocalContext.current
                    Log.d(
                        "nowTime",
                        "${alarm.timeInMillis} , ${Date(alarm.timeInMillis).hours}:${Date(alarm.timeInMillis).minutes} "
                    )
                    val cardColor by animateColorAsState(
                        targetValue = if (checkedList.contains(alarm)) colorResource(R.color.checkedCard) else colorResource(
                            R.color.alarmBackground
                        ),
                        animationSpec = tween(100), label = ""
                    )
                    val newHorizontalPadding by animateIntAsState(
                        targetValue = if (checkedList.contains(alarm)) 10 else 0,
                        animationSpec = tween(100), label = ""
                    )
                    val newVerticalPadding by animateIntAsState(
                        targetValue = if (checkedList.contains(alarm)) 4 else 0,
                        animationSpec = tween(100), label = ""
                    )
                    Row(
                        modifier = modifier
                            .fillMaxWidth()
                            .height(100.dp)
                            .clip(RoundedCornerShape(18.dp))
                            .background(cardColor)
                            .combinedClickable(
                                onLongClick = {
                                    if (checkedList.isEmpty()) {
                                        checkedList.add(alarm)
                                        isVisible = true
                                    } else {
                                        if (checkedList.contains(alarm)) {
                                            checkedList.remove(alarm)
                                        } else {
                                            checkedList.add(alarm)
                                        }
                                    }
                                }
                            ) {
                                if (isVisible) {
                                    if (checkedList.contains(alarm)) {
                                        checkedList.remove(alarm)
                                    } else {
                                        checkedList.add(alarm)
                                    }
                                } else {
                                    onSetAction(SetAlarmEvents.AddWholeAlarm(alarm))
                                    onSetAction(SetAlarmEvents.SetOldAlarm(alarm))
                                    navController.navigate(Routes.SetAlarm.route)
                                }
                            }
                            .padding(horizontal = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column (
                            modifier = Modifier.fillMaxWidth(.76f)
                        ){
                            Text(
                                text = buildAnnotatedString {
                                    append(SimpleDateFormat("hh:mm").format(Date(alarm.timeInMillis)))
                                    withStyle(
                                        style = SpanStyle(
                                            fontSize = 15.sp,
                                            color = if (alarm.status) colorResource(R.color.timeOnColor) else colorResource(
                                                R.color.timeOffColor
                                            )
                                        ),
                                    ) {
                                        append("  " + alarm.additionalInfo)
                                    }
                                },
                                fontSize = 35.sp,
                                color = if (alarm.status) colorResource(R.color.timeOnColor) else colorResource(
                                    R.color.timeOffColor
                                )
                            )
                            Text(
                                text =
                                if (alarm.description.length <= 47) alarm.description
                                else alarm.description.slice(0..46) + "...",
                                fontSize = 15.sp,
                                color = if (alarm.status) colorResource(R.color.desOnColor) else colorResource(
                                    R.color.desOffColor
                                ),
                            )
                        }
                        AnimatedContent(targetState = isVisible) { isVisible ->
                            when (isVisible) {
                                true -> {
                                    FilledTonalIconToggleButton(
                                        checked = checkedList.contains(alarm),
                                        onCheckedChange = {
                                            if (it) {
                                                checkedList.add(alarm)
                                            } else {
                                                checkedList.remove(alarm)
                                            }
                                        },
                                        colors = IconToggleButtonColors(
                                            containerColor = colorResource(R.color.lightGray),
                                            contentColor = Color.Transparent,
                                            checkedContainerColor = colorResource(R.color.lightBlue),
                                            checkedContentColor = Color.White,
                                            disabledContentColor = Color.Unspecified,
                                            disabledContainerColor = Color.Unspecified
                                        ),
                                        modifier = Modifier.size(30.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Filled.Check,
                                            contentDescription = null,
                                            modifier = Modifier.size(23.dp)
                                        )
                                    }
                                }

                                false -> {
                                    Switch(
                                        checked = alarm.status,
                                        onCheckedChange = {
                                            onEvent(AlarmEvents.UpdateAlarm(alarm.copy(status = it)))
                                            if (it) {
//                                                this check to make alarm tomorrow if our current time passed alarm defined
                                                if (dateTime.atZone(ZoneId.systemDefault())
                                                        .toEpochSecond() * 1000 >= alarm.timeInMillis
                                                ) {
                                                    isScheduled = true
                                                    GlobalScope.launch {
                                                        onEvent(
                                                            AlarmEvents.UpdateAlarm(
                                                                alarm.copy(
                                                                    newTimeMillis = newTimeInMillis,
                                                                    status = true
                                                                )
                                                            )
                                                        )
                                                        onSetAction(
                                                            SetAlarmEvents.ScheduleAlarm(
                                                                alarm.copy(
                                                                    timeInMillis = newTimeInMillis,
                                                                    status = true,
                                                                    newTimeMillis = newTimeInMillis
                                                                )
                                                            )
                                                        )
                                                    }
                                                    if (alarm.hour > dateTime.hour) {
                                                        Toast.makeText(
                                                            context,
                                                            "Alarm will ring today at ${alarm.hour}:${alarm.minute} ${alarm.additionalInfo}",
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                    }
                                                    else if (alarm.hour < dateTime.hour) {
                                                        Toast.makeText(
                                                            context,
                                                            "Alarm will ring tomorrow at ${alarm.hour}:${alarm.minute} ${alarm.additionalInfo}",
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                    }
                                                    else {
                                                        if (alarm.minute > dateTime.minute) {
                                                            Toast.makeText(
                                                                context,
                                                                "Alarm will ring today at ${alarm.hour}:${alarm.minute} ${alarm.additionalInfo}",
                                                                Toast.LENGTH_SHORT
                                                            ).show()
                                                        }
                                                        else {
                                                            Toast.makeText(
                                                                context,
                                                                "Alarm will ring tomorrow at ${alarm.hour}:${alarm.minute} ${alarm.additionalInfo}",
                                                                Toast.LENGTH_SHORT
                                                            ).show()
                                                        }
                                                    }
                                                }
                                                else {
                                                    GlobalScope.launch {
                                                        onSetAction(
                                                            SetAlarmEvents.ScheduleAlarm(
                                                                alarm.copy(
                                                                    status = true
                                                                )
                                                            )
                                                        )

                                                    }
                                                    if (alarm.hour > dateTime.hour) {
                                                        Toast.makeText(
                                                            context,
                                                            "Alarm will ring today at ${alarm.hour}:${alarm.minute} ${alarm.additionalInfo}",
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                    }
                                                    else if (alarm.hour < dateTime.hour) {
                                                        Toast.makeText(
                                                            context,
                                                            "Alarm will ring tomorrow at ${alarm.hour}:${alarm.minute} ${alarm.additionalInfo}",
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                    }
                                                    else {
                                                        if (alarm.minute > dateTime.minute) {
                                                            Toast.makeText(
                                                                context,
                                                                "Alarm will ring today at ${alarm.hour}:${alarm.minute} ${alarm.additionalInfo}",
                                                                Toast.LENGTH_SHORT
                                                            ).show()
                                                        } else {
                                                            Toast.makeText(
                                                                context,
                                                                "Alarm will ring tomorrow at ${alarm.hour}:${alarm.minute} ${alarm.additionalInfo}",
                                                                Toast.LENGTH_SHORT
                                                            ).show()
                                                        }
                                                    }

                                                }
                                            }
                                            else {
                                                if (alarm.newTimeMillis == -1L || alarm.newTimeMillis == -2L) {
                                                    GlobalScope.launch {
                                                        onSetAction(
                                                            SetAlarmEvents.StopAlarm(
                                                                alarm.copy(
                                                                    status = true
                                                                )
                                                            )
                                                        )
                                                    }

                                                } else {
                                                    GlobalScope.launch {
                                                        onSetAction(
                                                            SetAlarmEvents.StopAlarm(
                                                                alarm.copy(
                                                                    timeInMillis = alarm.newTimeMillis,
                                                                    status = true,
                                                                    newTimeMillis = alarm.newTimeMillis
                                                                )
                                                            )
                                                        )
                                                    }
                                                }
                                            }
                                        },
                                        colors = SwitchDefaults.colors(
                                            checkedBorderColor = Color.Transparent,
                                            uncheckedBorderColor = Color.Transparent,
                                            uncheckedThumbColor = Color.White,
                                            checkedThumbColor = Color.White,
                                            checkedTrackColor = colorResource(R.color.lightBlue)

                                        ),
                                        thumbContent = {}
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


