@file:Suppress("DEPRECATED_ANNOTATION")

package com.example.alarm.Data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.alarm.R
import kotlinx.android.parcel.Parcelize
import java.time.LocalDateTime
import java.time.LocalTime


@Entity
@Parcelize
data class AlarmData(
   @PrimaryKey(autoGenerate = false) val timeInMillis :  Long = 0,
    val additionalInfo : String = "",
    val description : String = "",
    val status : Boolean = true,
    val snoozeMode : Boolean = false,
    val ringTone : Int = R.raw.drama,
    val hourMultiplyMinute : Int = 0,
    val newTimeMillis : Long  = -1L,
    val hour : Int = 0,
    val minute : Int = 0,
    val timeInText : String = ""

) : Parcelable


