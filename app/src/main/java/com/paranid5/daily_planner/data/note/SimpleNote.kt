package com.paranid5.daily_planner.data.note

import android.os.Parcelable
import com.paranid5.daily_planner.data.note.Note
import kotlinx.parcelize.Parcelize

@Parcelize
data class SimpleNote(
    override val id: Int,
    override val title: String,
    override val description: String,
    override val isDone: Boolean = false
) : Note, Parcelable {
    override val message: String
        get() = description
}
