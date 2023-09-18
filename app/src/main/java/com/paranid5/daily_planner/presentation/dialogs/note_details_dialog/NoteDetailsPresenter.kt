package com.paranid5.daily_planner.presentation.dialogs.note_details_dialog

import android.view.View
import com.paranid5.daily_planner.data.note.DatedNote
import com.paranid5.daily_planner.data.note.Note
import com.paranid5.daily_planner.data.note.SimpleNote
import com.paranid5.daily_planner.presentation.ObservablePresenter
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

data class NoteDetailsPresenter @AssistedInject constructor(
    @Assisted("note") val note: Note,
) : ObservablePresenter() {
    @JvmField
    val dateElementsVisibility = when (note) {
        is DatedNote -> View.VISIBLE
        is SimpleNote -> View.GONE
    }
}