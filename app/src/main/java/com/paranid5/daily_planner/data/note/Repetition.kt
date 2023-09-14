package com.paranid5.daily_planner.data.note

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed interface Repetition : Parcelable {
    companion object {
        fun fromOrdinal(ordinal: Int) = when (ordinal) {
            0 -> NoRepetition
            1 -> Days(days = WeekDay.entries.toTypedArray()) // TODO: days selection
            2 -> Weekly
            3 -> Monthly
            4 -> Yearly
            else -> throw IllegalArgumentException("Illegal repetition index")
        }
    }

    @Parcelize
    data object NoRepetition : Repetition

    @Parcelize
    data class Days(val days: Array<WeekDay>) : Repetition

    @Parcelize
    data object Weekly : Repetition

    @Parcelize
    data object Monthly : Repetition

    @Parcelize
    data object Yearly : Repetition
}

inline val Repetition.ordinal
    get() = when (this) {
        Repetition.NoRepetition -> 0
        is Repetition.Days -> 1
        Repetition.Monthly -> 2
        Repetition.Weekly -> 3
        Repetition.Yearly -> 4
    }