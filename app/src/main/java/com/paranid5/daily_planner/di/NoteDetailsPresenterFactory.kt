package com.paranid5.daily_planner.di

import com.paranid5.daily_planner.data.note.Note
import com.paranid5.daily_planner.presentation.dialogs.note_details_dialog.NoteDetailsPresenter
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory

@AssistedFactory
interface NoteDetailsPresenterFactory {
    fun create(@Assisted("note") note: Note): NoteDetailsPresenter
}