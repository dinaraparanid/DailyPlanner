package com.paranid5.daily_planner.data.note

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed interface Repetition : Parcelable {
    companion object {
        fun fromOrdinal(ordinal: Int) = when (ordinal) {
            0 -> NoRepetition
            1 -> Daily // TODO: days selection
            2 -> Weekly
            3 -> Monthly
            4 -> Yearly
            else -> throw IllegalArgumentException("Illegal repetition index")
        }
    }

    @Parcelize
    data object NoRepetition : Repetition

    @Parcelize
    data object Daily : Repetition

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
        Repetition.Daily -> 1
        Repetition.Monthly -> 2
        Repetition.Weekly -> 3
        Repetition.Yearly -> 4
    }