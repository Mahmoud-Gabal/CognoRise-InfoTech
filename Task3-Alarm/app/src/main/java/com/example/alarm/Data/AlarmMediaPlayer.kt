package com.example.alarm.Data

import android.content.Context
import android.media.MediaPlayer
import com.example.alarm.R

class AlarmMediaPlayer(
    val context: Context,
) {

    val mediaPlayerDrama = MediaPlayer.create(context, R.raw.drama)
    val mediaPlayerFunny = MediaPlayer.create(context, R.raw.funny)
    val mediaPlayerNight = MediaPlayer.create(context, R.raw.night)
    val mediaPlayerDream = MediaPlayer.create(context, R.raw.dream)

    fun startDrama(){
        mediaPlayerDrama.start()
    }

    fun stopDrama(){
        mediaPlayerDrama.stop()
        mediaPlayerDrama.prepare()
    }

    fun startFunny(){
        mediaPlayerFunny.start()
    }

    fun stopFunny(){
        mediaPlayerFunny.stop()
        mediaPlayerFunny.prepare()
    }

    fun startNight(){
        mediaPlayerNight.start()
    }

    fun stopNight(){
        mediaPlayerNight.stop()
        mediaPlayerNight.prepare()

    }

    fun startDream(){
        mediaPlayerDream.start()
    }

    fun stopDream(){
        mediaPlayerDream.stop()
        mediaPlayerDream.prepare()
    }





}