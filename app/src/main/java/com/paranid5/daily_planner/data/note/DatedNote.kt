package com.paranid5.daily_planner.data.note

import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import com.paranid5.daily_planner.data.Repetition
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

internal inline val currentTime
    get() = Clock.System.now()
        .toLocalDateTime(TimeZone.currentSystemDefault())

data class DatedNote(
    override val id: Int,
    override val title: String,
    override val description: String,
    override val isDone: Boolean = false,
    @JvmField val date: LocalDateTime = currentTime,
    @JvmField val repetition: Repetition = Repetition.NoRepetition,
) : Note, Parcelable {
    companion object CREATOR : Parcelable.Creator<DatedNote> {
        override fun createFromParcel(parcel: Parcel) = DatedNote(parcel)
        override fun newArray(size: Int): Array<DatedNote?> = arrayOfNulls(size)
    }

    constructor(parcel: Parcel) : this(
        id = parcel.readInt(),
        title = parcel.readString() ?: "",
        description = parcel.readString() ?: "",
        date = parcel.readInt().takeIf { it != -1 }?.let {
            LocalDateTime(
                year = it,
                monthNumber = parcel.readInt(),
                dayOfMonth = parcel.readInt(),
                hour = parcel.readInt(),
                minute = parcel.readInt()
            )
        } ?: currentTime,
        repetition = when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU ->
                parcel.readParcelable(Repetition::class.java.classLoader, Repetition::class.java)

            else -> parcel.readParcelable(Repetition::class.java.classLoader)
        } ?: Repetition.NoRepetition,
        isDone = parcel.readByte() != 0.toByte()
    )

    override val message
        get() = dateMessage

    inline val dateMessage
        get() = "$dayMessage $timeMessage"

    inline val dayMessage
        get() = "${date.dayOfMonth}.${date.monthNumber}"

    inline val timeMessage
        get() = "${date.hour} : ${date.minute}"

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(title)
        parcel.writeString(description)

        date.run {
            parcel.writeInt(year)
            parcel.writeInt(monthNumber)
            parcel.writeInt(dayOfMonth)
            parcel.writeInt(hour)
            parcel.writeInt(minute)
        }

        parcel.writeParcelable(repetition, flags)
        parcel.writeByte(if (isDone) 1 else 0)
    }

    override fun describeContents() = 0
}
