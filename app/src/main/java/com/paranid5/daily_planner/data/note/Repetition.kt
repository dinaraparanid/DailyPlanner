package com.paranid5.daily_planner.data.note

import android.os.Parcel
import android.os.Parcelable
import com.paranid5.daily_planner.data.utils.ext.readLocalDataTime
import com.paranid5.daily_planner.data.utils.ext.writeLocalDateTime
import com.paranid5.daily_planner.domain.utils.ext.currentTime
import kotlinx.datetime.LocalDateTime
import kotlinx.parcelize.Parcelize

sealed interface Repetition : Parcelable {
    companion object {
        fun fromOrdinal(ordinal: Int) = when (ordinal) {
            0 -> NoRepetition
            1 -> Daily
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

    // TODO: Handle specific time

    data class SpecificTime(val time: LocalDateTime) : Repetition {
        companion object CREATOR : Parcelable.Creator<SpecificTime> {
            override fun createFromParcel(parcel: Parcel) = SpecificTime(parcel)
            override fun newArray(size: Int) = arrayOfNulls<SpecificTime?>(size)
        }

        constructor(parcel: Parcel) : this(time = parcel.readLocalDataTime() ?: currentTime)

        override fun writeToParcel(parcel: Parcel, flags: Int) =
            parcel.writeLocalDateTime(time)

        override fun describeContents() = 0
    }
}

inline val Repetition.ordinal
    get() = when (this) {
        Repetition.NoRepetition -> 0
        Repetition.Daily -> 1
        Repetition.Weekly -> 2
        Repetition.Monthly -> 3
        Repetition.Yearly -> 4
        is Repetition.SpecificTime -> 5
    }