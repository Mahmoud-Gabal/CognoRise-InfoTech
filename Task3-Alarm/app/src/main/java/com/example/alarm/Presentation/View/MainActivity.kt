package com.example.alarm.Presentation.View

import android.Manifest
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.app.ActivityCompat
import com.example.alarm.Data.AlarmMediaPlayer
import com.example.alarm.Presentation.View.NavGraph.NavGraph
import com.example.alarm.Presentation.View.theme.AlarmTheme
import com.example.alarm.R
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject lateinit var alarmMediaPlayer: AlarmMediaPlayer
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//    to request for notification permission
    if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.TIRAMISU){
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.POST_NOTIFICATIONS),
            0
        )
    }
    enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(
                Color.Transparent.toArgb(),
                Color.Transparent.toArgb()
            ),
            navigationBarStyle = SystemBarStyle.light(
               getColor(R.color.homeBackground),
                getColor(R.color.homeBackground)
            )
        )
        setContent {
            AlarmTheme {
                NavGraph()
            }
        }
    }

}


