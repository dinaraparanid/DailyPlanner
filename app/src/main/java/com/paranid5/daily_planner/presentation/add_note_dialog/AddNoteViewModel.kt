package com.paranid5.daily_planner.presentation.add_note_dialog

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import com.paranid5.daily_planner.data.Note
import com.paranid5.daily_planner.di.AddNotePresenterFactory
import com.paranid5.daily_planner.presentation.ObservableViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddNoteViewModel @Inject constructor(
    presenterFactory: AddNotePresenterFactory,
    override val handler: AddNoteUIHandler,
    private val savedStateHandle: SavedStateHandle
) : ObservableViewModel<AddNotePresenter, AddNoteUIHandler>() {
    private companion object {
        private const val TITLE = "title"
        private const val DESCRIPTION = "description"
    }

    override val presenter = presenterFactory.create(
        titleInputState = savedStateHandle.getLiveData(TITLE, ""),
        descriptionInputState = savedStateHandle.getLiveData(DESCRIPTION, "")
    )

    inline val notesState: LiveData<List<Note>>
        get() = presenter.notesState

    inline val notes
        get() = notesState.value!!

    fun postNotes(notes: List<Note>) =
        presenter.notesState.postValue(notes)

    fun addNote(note: Note) = postNotes(notes + note)

    inline val titleInputState: LiveData<String>
        get() = presenter.titleInputState

    inline val titleInput
        get() = titleInputState.value!!

    fun postTitleInput(inp: CharSequence) {
        val input = inp.toString()
        presenter.titleInputState.postValue(input)
        savedStateHandle[TITLE] = input
    }

    inline val descriptionInputState: LiveData<String>
        get() = presenter.descriptionInputState

    inline val descriptionInput
        get() = descriptionInputState.value!!

    fun postDescriptionInput(inp: CharSequence) {
        val input = inp.toString()
        presenter.descriptionInputState.postValue(input)
        savedStateHandle[DESCRIPTION] = input
    }
}