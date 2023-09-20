package com.paranid5.daily_planner.data.utils.ext

import android.os.Build
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toJavaInstant
import java.util.Date

inline val LocalDateTime.epochMillis
    get() = toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds()

fun LocalDateTime.toDate() = when {
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ->
        Date.from(toInstant(TimeZone.currentSystemDefault()).toJavaInstant())

    else -> Date(year, monthNumber, dayOfMonth, hour, minute)
}