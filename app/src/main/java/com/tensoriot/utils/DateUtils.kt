package com.tensoriot.utils

import android.text.format.DateFormat
import java.util.*

object DateUtils {

    fun convertTimeStampToDay(timestamp: Long):String {
        val cal: Calendar = Calendar.getInstance(Locale.ENGLISH)
        cal.timeInMillis = timestamp * 1000L
        val date: String = DateFormat.format("E", cal).toString()
        return date
    }
}