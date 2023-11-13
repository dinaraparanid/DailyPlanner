package com.paranid5.daily_planner.data.utils.ext

import android.os.Parcel
import kotlinx.datetime.LocalDateTime

fun Parcel.readLocalDataTime() = readInt().takeIf { it != -1 }?.let { year ->
    LocalDateTime(
        year = year,
        monthNumber = readInt(),
        dayOfMonth = readInt(),
        hour = readInt(),
        minute = readInt()
    )
}

fun Parcel.writeLocalDateTime(date: LocalDateTime) = date.run {
    writeInt(year)
    writeInt(monthNumber)
    writeInt(dayOfMonth)
    writeInt(hour)
    writeInt(minute)
}