package com.paranid5.daily_planner.di

import androidx.lifecycle.SavedStateHandle
import com.paranid5.daily_planner.data.note.Note
import com.paranid5.daily_planner.presentation.edit_note_dialog.EditNoteViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory

@AssistedFactory
interface EditNoteViewModelFactory {
    fun create(
        @Assisted savedStateHandle: SavedStateHandle,
        @Assisted initialNote: Note
    ): EditNoteViewModel
}