package com.paranid5.daily_planner.presentation.main_fragment

import androidx.lifecycle.SavedStateHandle
import com.paranid5.daily_planner.data.Note
import com.paranid5.daily_planner.di.MainFragmentPresenterFactory
import com.paranid5.daily_planner.presentation.ObservableViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainFragmentViewModel @Inject constructor(
    presenterFactory: MainFragmentPresenterFactory,
    override val handler: MainFragmentUIHandler,
    private val savedStateHandle: SavedStateHandle
) : ObservableViewModel<MainFragmentPresenter, MainFragmentUIHandler>() {
    private companion object {
        private const val NOTES = "notes"
    }

    override val presenter = presenterFactory.create(
        notesState = savedStateHandle.getLiveData(NOTES, listOf())
    )

    inline val notesState
        get() = presenter.notesState

    inline val notes
        get() = notesState.value!!

    fun postNotes(notes: List<Note>) {
        presenter.notesState.postValue(notes)
        savedStateHandle[NOTES] = notes
    }
}