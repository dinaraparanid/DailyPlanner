package com.paranid5.daily_planner.di

import com.paranid5.daily_planner.data.note.Note
import com.paranid5.daily_planner.presentation.note_details_dialog.NoteDetailsViewModel
import dagger.assisted.AssistedFactory

@AssistedFactory
interface NoteDetailsViewModelFactory {
    fun create(note: Note): NoteDetailsViewModel
}