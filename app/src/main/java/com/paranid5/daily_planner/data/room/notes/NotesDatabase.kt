package com.paranid5.daily_planner.data.room.notes

import androidx.room.Database
import androidx.room.RoomDatabase
import com.paranid5.daily_planner.data.note.DatedNote
import com.paranid5.daily_planner.data.note.SimpleNote

@Database(entities = [SimpleNote::class, DatedNote.Entity::class], version = 1)
abstract class NotesDatabase : RoomDatabase() {
    abstract fun simpleNotesDao(): SimpleNotesDao
    abstract fun datedNotesDao(): DatedNotesDao
}