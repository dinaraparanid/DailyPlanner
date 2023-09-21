package com.paranid5.daily_planner.data.room.notes

import android.content.Context
import androidx.room.Room
import com.paranid5.daily_planner.data.note.DatedNote
import com.paranid5.daily_planner.data.note.Note
import com.paranid5.daily_planner.data.note.SimpleNote
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotesRepository @Inject constructor(@ApplicationContext context: Context) {
    private companion object {
        private const val DATABASE_NAME = "notes"
    }

    private val db = Room.databaseBuilder(
        context,
        NotesDatabase::class.java,
        DATABASE_NAME
    ).build()

    private val simpleNotesDao = db.simpleNotesDao()
    private val datedNotesDao = db.datedNotesDao()

    val simpleNotes
        get() = simpleNotesDao.getAll()

    fun getSimpleNoteById(id: Int) = simpleNotesDao.getById(id)

    internal suspend inline fun insert(note: SimpleNote) =
        simpleNotesDao.insert(note)

    internal suspend inline fun update(updatedNote: SimpleNote) =
        simpleNotesDao.update(updatedNote)

    internal suspend inline fun update(initial: SimpleNote, upd: (SimpleNote) -> SimpleNote) =
        simpleNotesDao.update(upd(initial))

    internal suspend inline fun changeChecked(note: SimpleNote, isChecked: Boolean) =
        update(note) { it.copy(isDone = isChecked) }

    internal suspend inline fun delete(note: SimpleNote) =
        simpleNotesDao.delete(note)

    val datedNotes
        get() = datedNotesDao.getAll().map { it.map(::DatedNote) }

    fun getDatedNoteById(id: Int) = datedNotesDao.getById(id)

    internal suspend inline fun insert(note: DatedNote) =
        datedNotesDao.insert(note.entity)

    internal suspend inline fun update(updatedNote: DatedNote) =
        datedNotesDao.update(updatedNote.entity)

    internal suspend inline fun update(initial: DatedNote, upd: (DatedNote) -> DatedNote) =
        datedNotesDao.update(upd(initial).entity)

    internal suspend inline fun changeChecked(note: DatedNote, isChecked: Boolean) =
        update(note) { it.copy(isDone = isChecked) }

    internal suspend inline fun delete(note: DatedNote) =
        datedNotesDao.delete(note.entity)

    internal suspend inline fun changeChecked(note: Note, isChecked: Boolean) = when (note) {
        is DatedNote -> changeChecked(note, isChecked)
        is SimpleNote -> changeChecked(note, isChecked)
    }
}