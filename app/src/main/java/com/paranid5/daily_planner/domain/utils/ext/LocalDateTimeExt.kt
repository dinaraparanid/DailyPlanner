package com.paranid5.daily_planner.domain.utils.ext

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import java.util.Date
import kotlin.time.Duration.Companion.days

inline val LocalDateTime.epochMillis
    get() = toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds()

inline val LocalDateTime.epochSeconds
    get() = toInstant(TimeZone.currentSystemDefault()).epochSeconds

fun LocalDateTime.toDate() = Date(epochMillis)

fun LocalDateTime.toInstant() = toInstant(TimeZone.currentSystemDefault())

fun LocalDateTime.addMonth(): LocalDateTime? {
    val try1 = runCatching {
        LocalDateTime(
            year = year,
            monthNumber = monthNumber + 1,
            dayOfMonth = dayOfMonth,
            hour = hour,
            minute = minute
        )
    }

    if (try1.isSuccess)
        return try1.getOrNull()!!

    if (monthNumber == 12) {
        val try2 = runCatching {
            LocalDateTime(
                year = year + 1,
                monthNumber = 1,
                dayOfMonth = dayOfMonth,
                hour = hour,
                minute = minute
            )
        }

        if (try2.isSuccess)
            return try2.getOrNull()!!
    }

    return null
}

fun LocalDateTime.addYear() = runCatching {
    LocalDateTime(
        year = year + 1,
        monthNumber = monthNumber,
        dayOfMonth = dayOfMonth,
        hour = hour,
        minute = minute
    )
}.getOrNull()