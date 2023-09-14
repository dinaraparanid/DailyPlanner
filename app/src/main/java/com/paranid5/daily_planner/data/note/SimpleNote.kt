package com.paranid5.daily_planner.data.note

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity as RoomEntity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import com.paranid5.daily_planner.data.room.Entity

@Parcelize
@RoomEntity
data class SimpleNote(
    @PrimaryKey(autoGenerate = true) override val id: Int,
    override val title: String,
    override val description: String,
    @ColumnInfo("is_done") override val isDone: Boolean = false
) : Note, Entity, Parcelable {
    constructor(title: String, description: String, isDone: Boolean = false) : this(
        id = 0,
        title = title,
        description = description,
        isDone = isDone
    )

    override val message: String
        get() = description
}
