package com.paranid5.daily_planner.data.note

import android.os.Parcelable

sealed interface Note : Parcelable {
    val id: Int
    val title: String
    val description: String
    val isDone: Boolean
    val message: String
}