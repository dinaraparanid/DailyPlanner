package com.paranid5.daily_planner.data.utils.ext

import androidx.annotation.StringRes
import com.paranid5.daily_planner.R
import com.paranid5.daily_planner.data.note.DatedNote
import com.paranid5.daily_planner.data.note.Note
import com.paranid5.daily_planner.data.note.Repetition
import com.paranid5.daily_planner.data.note.SimpleNote
import com.paranid5.daily_planner.domain.utils.ext.addMonth
import com.paranid5.daily_planner.domain.utils.ext.addYear
import com.paranid5.daily_planner.domain.utils.ext.epochSeconds
import com.paranid5.daily_planner.domain.utils.ext.toInstant
import com.paranid5.daily_planner.domain.utils.ext.toLocalDateTime
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.seconds

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
            Repetition.Daily -> R.string.daily
            Repetition.Weekly -> R.string.weekly
            Repetition.Monthly -> R.string.monthly
            Repetition.Yearly -> R.string.yearly
            is Repetition.SpecificTime -> R.string.specific_time
        }

        is SimpleNote -> R.string.no_repetition
    }

inline val DatedNote.nextAlarmTime: LocalDateTime?
    get() = when (repetition) {
        Repetition.NoRepetition -> null

        Repetition.Daily -> date.toInstant().plus(1.days).toLocalDateTime()

        Repetition.Weekly -> date.toInstant().plus(7.days).toLocalDateTime()

        Repetition.Monthly -> date.addMonth()

        Repetition.Yearly -> date.addYear()

        is Repetition.SpecificTime -> date
            .toInstant()
            .plus(repetition.time.epochSeconds.seconds)
            .toLocalDateTime()
    }