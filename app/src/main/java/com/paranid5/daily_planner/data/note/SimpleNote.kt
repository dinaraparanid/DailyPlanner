package com.paranid5.daily_planner.data.note

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import com.paranid5.daily_planner.data.room.Entity
import kotlinx.parcelize.Parcelize
import androidx.room.Entity as RoomEntity

@Parcelize
@RoomEntity
data class SimpleNote(
    @PrimaryKey(autoGenerate = true) override val id: Long,
    override val title: String,
    override val description: String,
    @ColumnInfo("is_done") override val isDone: Boolean = false
) : Note, Entity {
    constructor(title: String, description: String, isDone: Boolean = false) : this(
        id = 0,
        title = title,
        description = description,
        isDone = isDone
    )

    override val message: String
        get() = description
}
