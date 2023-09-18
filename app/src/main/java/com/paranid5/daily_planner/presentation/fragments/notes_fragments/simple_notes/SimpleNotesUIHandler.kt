package com.paranid5.daily_planner.presentation.fragments.notes_fragments.simple_notes

import com.paranid5.daily_planner.data.note.NoteType
import com.paranid5.daily_planner.presentation.fragments.notes_fragments.NotesUIHandler
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SimpleNotesUIHandler @Inject constructor() : NotesUIHandler {
    override val notesType get() = NoteType.SIMPLE
}