package com.paranid5.daily_planner.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed interface Repetition : Parcelable {
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