package com.paranid5.daily_planner.presentation.edit_note_dialog

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import com.paranid5.daily_planner.data.utils.ext.repetitionOrNull
import com.paranid5.daily_planner.data.note.DatedNote
import com.paranid5.daily_planner.data.note.Note
import com.paranid5.daily_planner.data.note.Repetition
import com.paranid5.daily_planner.data.note.SimpleNote
import com.paranid5.daily_planner.data.room.notes.NotesRepository
import com.paranid5.daily_planner.di.EditNotePresenterFactory
import com.paranid5.daily_planner.di.EditNoteViewModelFactory
import com.paranid5.daily_planner.presentation.ObservableViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

class EditNoteViewModel @AssistedInject constructor(
    presenterFactory: EditNotePresenterFactory,
    override val handler: EditNoteUIHandler,
    private val notesRepository: NotesRepository,
    @Assisted private val savedStateHandle: SavedStateHandle,
    @Assisted initialNote: Note
) : ObservableViewModel<EditNotePresenter, EditNoteUIHandler>() {
    companion object {
        private const val TITLE = "title"
        private const val DESCRIPTION = "description"
        private const val REPETITION = "repetition"

        fun provideFactory(
            assistedFactory: EditNoteViewModelFactory,
            initialNote: Note
        ) = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras) =
                assistedFactory.create(
                    savedStateHandle = extras.createSavedStateHandle(),
                    initialNote = initialNote,
                ) as T
        }
    }

    override val presenter = presenterFactory.create(
        initialNote = initialNote,
        titleInputState = savedStateHandle.getLiveData(TITLE, initialNote.title),
        descriptionInputState = savedStateHandle.getLiveData(DESCRIPTION, initialNote.description),
        repetitionState = savedStateHandle.getLiveData(REPETITION, initialNote.repetitionOrNull ?: Repetition.NoRepetition)
    )

    inline val initialNote
        get() = presenter.initialNote

    inline val dateElementsVisibility
        get() = presenter.dateElementsVisibility

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

    private suspend inline fun updateSimpleNote(note: SimpleNote) =
        notesRepository.update(note) {
            it.copy(title = titleInput, description = descriptionInput)
        }

    private suspend inline fun updateDatedNote(note: DatedNote) =
        notesRepository.update(note) {
            it.copy(
                title = titleInput,
                description = descriptionInput,
                repetition = repetition
            )
        }

    internal suspend inline fun updateNote() = when (val note = initialNote) {
        is DatedNote -> updateDatedNote(note)
        is SimpleNote -> updateSimpleNote(note)
    }
}