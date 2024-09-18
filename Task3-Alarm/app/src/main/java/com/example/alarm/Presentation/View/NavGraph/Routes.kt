package com.example.alarm.Presentation.View.NavGraph

sealed class Routes(val route : String) {

    object HomeScreen : Routes("home")

    object SetAlarm : Routes("set alarm")

    object RepeatAlarm : Routes("repeat alarm")

    object RingTonePage: Routes("ring_tone")

}