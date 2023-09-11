package com.paranid5.daily_planner.presentation.main_fragment

import androidx.lifecycle.LiveData
import com.paranid5.daily_planner.data.note.Note
import com.paranid5.daily_planner.presentation.ObservableViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainFragmentViewModel @Inject constructor(
    override val presenter: MainFragmentPresenter,
    override val handler: MainFragmentUIHandler,
) : ObservableViewModel<MainFragmentPresenter, MainFragmentUIHandler>() {
    inline val notesState: LiveData<List<Note>>
        get() = presenter.notesState

    inline val notes
        get() = notesState.value!!

    fun postNotes(notes: List<Note>) =
        presenter.notesState.postValue(notes)
}