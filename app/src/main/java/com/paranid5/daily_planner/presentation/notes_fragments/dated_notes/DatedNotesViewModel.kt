package com.paranid5.daily_planner.presentation.notes_fragments.dated_notes

import androidx.lifecycle.LiveData
import com.paranid5.daily_planner.data.note.DatedNote
import com.paranid5.daily_planner.presentation.ObservableViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DatedNotesViewModel @Inject constructor(
    override val presenter: DatedNotesPresenter,
    override val handler: DatedNotesUIHandler
) : ObservableViewModel<DatedNotesPresenter, DatedNotesUIHandler>() {
    inline val notesState: LiveData<List<DatedNote>>
        get() = presenter.notesState

    inline val notes
        get() = notesState.value!!

    fun postNotes(notes: List<DatedNote>) =
        presenter.notesState.postValue(notes)
}