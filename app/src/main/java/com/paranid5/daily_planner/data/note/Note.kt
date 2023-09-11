package com.paranid5.daily_planner.data.note

sealed interface Note {
    val id: Int
    val title: String
    val description: String
    val isDone: Boolean
    val message: String
}