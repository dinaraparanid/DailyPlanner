package com.paranid5.daily_planner.data

import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import kotlinx.datetime.LocalDateTime

data class Note(
    @JvmField val id: Int,
    @JvmField val title: String,
    @JvmField val description: String,
    @JvmField val date: LocalDateTime? = null,
    @JvmField val repetition: Repetition = Repetition.NoRepetition,
    @JvmField val isDone: Boolean = false
) : Parcelable {
    companion object CREATOR : Parcelable.Creator<Note> {
        override fun createFromParcel(parcel: Parcel) = Note(parcel)
        override fun newArray(size: Int): Array<Note?> = arrayOfNulls(size)
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
        },
        repetition = when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU ->
                parcel.readParcelable(Repetition::class.java.classLoader, Repetition::class.java)

            else -> parcel.readParcelable(Repetition::class.java.classLoader)
        } ?: Repetition.NoRepetition,
        isDone = parcel.readByte() != 0.toByte()
    )

    inline val message
        get() = date?.let { dateMessage } ?: description

    inline val dateMessage
        get() = "$dayMessage $timeMessage"

    inline val dayMessage
        get() = "${date?.dayOfMonth}.${date?.monthNumber}"

    inline val timeMessage
        get() = "${date?.hour} : ${date?.minute}"

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(title)
        parcel.writeString(description)

        date?.run {
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
