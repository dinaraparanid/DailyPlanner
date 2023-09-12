package com.paranid5.daily_planner.presentation.notes_fragments.simple_notes

import androidx.lifecycle.LiveData
import com.paranid5.daily_planner.data.note.SimpleNote
import com.paranid5.daily_planner.presentation.ObservableViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SimpleNotesViewModel @Inject constructor(
    override val presenter: SimpleNotesPresenter,
    override val handler: SimpleNotesUIHandler,
) : ObservableViewModel<SimpleNotesPresenter, SimpleNotesUIHandler>() {
    inline val notesState: LiveData<List<SimpleNote>>
        get() = presenter.notesState

    inline val notes
        get() = notesState.value!!

    fun postNotes(notes: List<SimpleNote>) =
        presenter.notesState.postValue(notes)
}