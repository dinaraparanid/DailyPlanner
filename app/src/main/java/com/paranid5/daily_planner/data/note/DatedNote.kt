package com.paranid5.daily_planner.data.note

import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import androidx.room.Entity as RoomEntity
import com.paranid5.daily_planner.data.room.Entity as BaseEntity

private inline val currentTime
    get() = Clock.System.now()
        .toLocalDateTime(TimeZone.currentSystemDefault())

private inline val String.filledToTimeFormat
    get() = if (length < 2) "${"0".repeat(2 - length)}$this" else this

data class DatedNote(
    override val id: Int,
    override val title: String,
    override val description: String,
    @ColumnInfo("is_done") override val isDone: Boolean = false,
    @JvmField val date: LocalDateTime = currentTime,
    @JvmField val repetition: Repetition = Repetition.NoRepetition,
) : Note, Parcelable {
    companion object CREATOR : Parcelable.Creator<DatedNote> {
        override fun createFromParcel(parcel: Parcel) = DatedNote(parcel)
        override fun newArray(size: Int): Array<DatedNote?> = arrayOfNulls(size)
    }

    @RoomEntity(tableName = "DatedNote")
    data class Entity(
        @PrimaryKey(autoGenerate = true) val id: Int,
        val title: String,
        val description: String,
        @ColumnInfo("is_done") val isDone: Boolean,
        val year: Int,
        val month: Int,
        val day: Int,
        val hour: Int,
        val minute: Int,
        val repetition: Int,
    ) : BaseEntity {
        constructor(
            title: String,
            description: String,
            isDone: Boolean = false,
            year: Int,
            month: Int,
            day: Int,
            hour: Int,
            minute: Int,
            repetition: Int
        ) : this(
            id = 0,
            title = title,
            description = description,
            isDone = isDone,
            year = year,
            month = month,
            day = day,
            hour = hour,
            minute = minute,
            repetition = repetition
        )

        constructor(note: DatedNote) : this(
            id = note.id,
            title = note.title,
            description = note.description,
            isDone = note.isDone,
            year = note.date.year,
            month = note.date.monthNumber,
            day = note.date.dayOfMonth,
            hour = note.date.hour,
            minute = note.date.minute,
            repetition = note.repetition.ordinal
        )
    }

    constructor(entity: Entity) : this(
        id = entity.id,
        title = entity.title,
        description = entity.description,
        isDone = entity.isDone,
        date = LocalDateTime(
            year = entity.year,
            monthNumber = entity.month,
            dayOfMonth = entity.day,
            hour = entity.hour,
            minute = entity.minute
        ),
        repetition = Repetition.fromOrdinal(entity.repetition)
    )

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

    private inline val dateMessage
        get() = "$dayMessage $timeMessage"

    private inline val dayMessage
        get() = "${date.dayOfMonth.toString().filledToTimeFormat}.${date.monthNumber.toString().filledToTimeFormat}"

    private inline val timeMessage
        get() = "${date.hour}:${date.minute}"

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

    inline val entity
        get() = Entity(this)
}