package com.example.alarm.Presentation.View.NavGraph

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.alarm.Presentation.Intent.AlarmViewModel
import com.example.alarm.Presentation.Intent.SetAlarmViewModel
import com.example.alarm.Presentation.View.HomeScreen
import com.example.alarm.Presentation.View.RepeatPage
import com.example.alarm.Presentation.View.RingTonePage
import com.example.alarm.Presentation.View.SetAlarm


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavGraph(
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()


    val alarmViewModel  : AlarmViewModel = hiltViewModel()
    val alarmsList = alarmViewModel.alarmList.collectAsState().value

    val setAlarmViewModel : SetAlarmViewModel = hiltViewModel()
    val customDays = setAlarmViewModel.customDaysList
    val alarmData = setAlarmViewModel.alarmData.collectAsState().value
    val oldAlarm = setAlarmViewModel.oldAlarm.collectAsState().value




    NavHost(
        navController = navController,
        startDestination = Routes.HomeScreen.route
    ) {
        composable(route = Routes.HomeScreen.route){

            HomeScreen(
                alarms = alarmsList,
                onEvent = alarmViewModel::onEvent,
                navController = navController,
                onSetAction = setAlarmViewModel::onSetAction
            )
        }
        composable(
            route = Routes.SetAlarm.route ,

        ){
            SetAlarm(
                navController = navController,
                onEvent = alarmViewModel::onEvent,
                customDays = customDays,
                alarmData = alarmData,
                onSetAction = setAlarmViewModel::onSetAction,
                oldAlarm = oldAlarm
            )
        }
        composable(route = Routes.RepeatAlarm.route){
            RepeatPage(
                navController = navController,
                customDays = customDays,
                onSetAction = setAlarmViewModel::onSetAction
            )
        }
        composable(route = Routes.RingTonePage.route){
            RingTonePage(
                navController = navController,
                alarmData = alarmData,
                onSetAction = setAlarmViewModel::onSetAction
            )
        }


    }

}