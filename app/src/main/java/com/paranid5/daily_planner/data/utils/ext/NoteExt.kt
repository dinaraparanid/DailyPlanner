package com.paranid5.daily_planner.data.utils.ext

import android.support.annotation.StringRes
import com.paranid5.daily_planner.R
import com.paranid5.daily_planner.data.note.DatedNote
import com.paranid5.daily_planner.data.note.Note
import com.paranid5.daily_planner.data.note.Repetition
import com.paranid5.daily_planner.data.note.SimpleNote

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
            is Repetition.Days -> R.string.daily
            Repetition.Weekly -> R.string.weekly
            Repetition.Monthly -> R.string.monthly
            Repetition.Yearly -> R.string.yearly
        }

        is SimpleNote -> R.string.no_repetition
    }