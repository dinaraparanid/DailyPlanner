package com.paranid5.daily_planner.presentation.notes_fragments.dated_notes

import com.paranid5.daily_planner.data.note.NoteType
import com.paranid5.daily_planner.presentation.notes_fragments.NotesUIHandler
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DatedNotesUIHandler @Inject constructor() : NotesUIHandler {
    override val notesType get() = NoteType.DATED
}