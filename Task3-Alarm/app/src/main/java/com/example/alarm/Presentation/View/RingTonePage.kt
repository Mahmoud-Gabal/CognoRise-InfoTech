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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.alarm.Data.AlarmData
import com.example.alarm.Presentation.Intent.SetAlarmEvents
import com.example.alarm.Presentation.View.Constants.daysOfWeek
import com.example.alarm.Presentation.View.NavGraph.Routes
import com.example.alarm.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RingTonePage(
    modifier: Modifier = Modifier,
    onSetAction: (SetAlarmEvents) -> Unit,
    navController: NavHostController = rememberNavController(),
    alarmData: AlarmData
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
                            tint = colorResource(R.color.titleText)
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
                            text = "Select ringtone",
                            color = colorResource(R.color.titleText),
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
                if (alarmData.ringTone==R.raw.drama) {
                    0
                } else if (alarmData.ringTone==R.raw.dream) {
                    1
                } else if (alarmData.ringTone==R.raw.funny) {
                    2
                } else {
                    3
                }
            val choices = listOf("Drama", "Dream", "Funny","Night")
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
                                    R.color.cardBackground
                                )
                            )
                            .clickable {
                                if (index==0)onSetAction(SetAlarmEvents.AddRingTone(R.raw.drama))
                                else if (index==1)onSetAction(SetAlarmEvents.AddRingTone(R.raw.dream))
                                else if (index==2)onSetAction(SetAlarmEvents.AddRingTone(R.raw.funny))
                                else onSetAction(SetAlarmEvents.AddRingTone(R.raw.night))
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
                                color = if (isChecked == index) colorResource(R.color.lightBlue) else colorResource(R.color.titleText)
                            )
                        }
                    }
                }
            }
        }
    }
}