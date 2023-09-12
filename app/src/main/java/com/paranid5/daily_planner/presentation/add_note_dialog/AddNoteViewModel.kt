package com.paranid5.daily_planner.presentation.add_note_dialog

import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import com.paranid5.daily_planner.data.Repetition
import com.paranid5.daily_planner.data.note.DatedNote
import com.paranid5.daily_planner.data.note.Note
import com.paranid5.daily_planner.data.note.NoteType
import com.paranid5.daily_planner.data.note.SimpleNote
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
        private const val NOTE_TYPE = "note_type"
        private const val TITLE = "title"
        private const val DESCRIPTION = "description"
        private const val REPETITION = "repetition"
    }

    override val presenter = presenterFactory.create(
        noteTypeState = savedStateHandle.getLiveData(NOTE_TYPE, NoteType.SIMPLE),
        titleInputState = savedStateHandle.getLiveData(TITLE, ""),
        descriptionInputState = savedStateHandle.getLiveData(DESCRIPTION, ""),
        repetitionState = savedStateHandle.getLiveData(REPETITION, Repetition.NoRepetition)
    )

    inline val simpleNotesState: LiveData<List<SimpleNote>>
        get() = presenter.simpleNotesState

    inline val datedNotesState: LiveData<List<DatedNote>>
        get() = presenter.datedNotesState

    inline val simpleNotes
        get() = simpleNotesState.value!!

    inline val datedNotes
        get() = datedNotesState.value!!

    fun postSimpleNotes(notes: List<SimpleNote>) =
        presenter.simpleNotesState.postValue(notes)

    fun postDatedNotes(notes: List<DatedNote>) =
        presenter.datedNotesState.postValue(notes)

    fun addNote(note: Note) = when (note) {
        is SimpleNote -> postSimpleNotes(simpleNotes + note)
        is DatedNote -> postDatedNotes(datedNotes + note)
    }

    inline val noteTypeState: LiveData<NoteType>
        get() = presenter.noteTypeState

    var noteType
        get() = noteTypeState.value!!

        @MainThread
        set(value) {
            presenter.noteType = value
            savedStateHandle[NOTE_TYPE] = value
        }

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

    inline val repetitionState: LiveData<Repetition>
        get() = presenter.repetitionState

    inline val repetition
        get() = repetitionState.value!!

    fun postRepetition(repetition: Repetition) {
        presenter.repetitionState.postValue(repetition)
        savedStateHandle[REPETITION] = repetition
    }
}