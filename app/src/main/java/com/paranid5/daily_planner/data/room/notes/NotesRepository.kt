package com.paranid5.daily_planner.data.room.notes

import android.content.Context
import androidx.room.Room
import com.paranid5.daily_planner.data.note.DatedNote
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

    internal suspend inline fun insert(vararg notes: SimpleNote) =
        simpleNotesDao.insert(*notes)

    internal suspend inline fun update(vararg initials: SimpleNote, upd: (SimpleNote) -> SimpleNote) =
        initials.map(upd).forEach { simpleNotesDao.update(it) }

    internal suspend inline fun delete(note: SimpleNote) =
        simpleNotesDao.delete(note)

    val datedNotes
        get() = datedNotesDao.getAll().map { it.map(::DatedNote) }

    fun getDatedNoteById(id: Int) = datedNotesDao.getById(id)

    internal suspend inline fun insert(vararg notes: DatedNote) =
        datedNotesDao.insert(*notes.map(DatedNote::entity).toTypedArray())

    internal suspend inline fun update(vararg initials: DatedNote, upd: (DatedNote) -> DatedNote) =
        initials
            .map { upd(it).entity }
            .forEach { datedNotesDao.update(it) }

    internal suspend inline fun delete(note: DatedNote) =
        datedNotesDao.delete(note.entity)
}