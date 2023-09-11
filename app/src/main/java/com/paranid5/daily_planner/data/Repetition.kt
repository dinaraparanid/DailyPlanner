package com.paranid5.daily_planner.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed interface Repetition : Parcelable {
    companion object {
        fun fromIndex(index: Int) = when (index) {
            0 -> NoRepetition
            1 -> Days(days = WeekDay.entries.toList()) // TODO: days selection
            2 -> Weekly
            3 -> Monthly
            4 -> Yearly
            else -> throw IllegalArgumentException("Illegal repetition index")
        }
    }

    @Parcelize
    data object NoRepetition : Repetition

    @Parcelize
    data class Days(val days: List<WeekDay>) : Repetition

    @Parcelize
    data object Weekly : Repetition

    @Parcelize
    data object Monthly : Repetition

    @Parcelize
    data object Yearly : Repetition
}