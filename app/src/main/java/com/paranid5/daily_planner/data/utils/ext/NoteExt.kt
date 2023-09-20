package com.paranid5.daily_planner.data.utils.ext

import android.support.annotation.StringRes
import com.paranid5.daily_planner.R
import com.paranid5.daily_planner.data.note.DatedNote
import com.paranid5.daily_planner.data.note.Note
import com.paranid5.daily_planner.data.note.Repetition
import com.paranid5.daily_planner.data.note.SimpleNote
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import java.util.Calendar
import kotlin.time.Duration.Companion.days

inline val Note.dateOrNull
    get() = when (this) {
        is DatedNote -> date
            .toInstant(TimeZone.currentSystemDefault())
            .toEpochMilliseconds()

        is SimpleNote -> null
    }

inline val Note.timeOrNull
    get() = when (this) {
        is DatedNote -> date.hour to date.minute
        is SimpleNote -> null
    }

inline val Note.repetitionOrNull
    get() = when (this) {
        is DatedNote -> repetition
        is SimpleNote -> null
    }

inline val Note.repetitionStrRes
    @StringRes
    get() = when (val note = this) {
        is DatedNote -> when (note.repetition) {
            Repetition.NoRepetition -> R.string.no_repetition
            is Repetition.Daily -> R.string.daily
            Repetition.Weekly -> R.string.weekly
            Repetition.Monthly -> R.string.monthly
            Repetition.Yearly -> R.string.yearly
        }

        is SimpleNote -> R.string.no_repetition
    }

inline val DatedNote.nextAlarmTime: LocalDateTime?
    get() {
        val calendar = Calendar.getInstance().apply {
            timeInMillis = 0
            time = date.toDate()
        }

        when (repetition) {
            Repetition.NoRepetition -> return null
            Repetition.Daily -> calendar.add(Calendar.DATE, 1)
            Repetition.Weekly -> calendar.add(Calendar.DATE, 7)
            Repetition.Monthly -> calendar.add(Calendar.MONTH, 1)
            Repetition.Yearly -> calendar.add(Calendar.YEAR, 1)
        }

        calendar.add(Calendar.MONTH, 1)
        return calendar.toLocalDateTime()
    }