package com.paranid5.daily_planner.presentation.dialogs.edit_note_dialog

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import com.paranid5.daily_planner.data.note.DatedNote
import com.paranid5.daily_planner.data.note.Note
import com.paranid5.daily_planner.data.note.Repetition
import com.paranid5.daily_planner.data.note.SimpleNote
import com.paranid5.daily_planner.data.room.notes.NotesRepository
import com.paranid5.daily_planner.data.utils.ext.dateOrNull
import com.paranid5.daily_planner.data.utils.ext.repetitionOrNull
import com.paranid5.daily_planner.data.utils.ext.timeOrNull
import com.paranid5.daily_planner.di.EditNotePresenterFactory
import com.paranid5.daily_planner.di.EditNoteViewModelFactory
import com.paranid5.daily_planner.presentation.ObservableViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.datetime.LocalDateTime
import java.util.Calendar
import java.util.Date

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
        private const val DATE = "date"
        private const val TIME = "time"
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
        dateState = savedStateHandle.getLiveData(DATE, initialNote.dateOrNull),
        timeState = savedStateHandle.getLiveData(TIME, initialNote.timeOrNull),
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

    inline val dateState: LiveData<Long?>
        get() = presenter.dateState

    inline val date
        get() = dateState.value!!

    fun postDate(date: Long) {
        presenter.dateState.postValue(date)
        savedStateHandle[DATE] = date
    }

    inline val timeState: LiveData<Pair<Int, Int>?>
        get() = presenter.timeState

    inline val time
        get() = timeState.value!!

    fun postTime(time: Pair<Int, Int>) {
        presenter.timeState.postValue(time)
        savedStateHandle[TIME] = time
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
            val calendar = Calendar
                .getInstance()
                .apply { time = Date(date) }

            it.copy(
                title = titleInput,
                description = descriptionInput,
                date = LocalDateTime(
                    year = calendar.get(Calendar.YEAR),
                    monthNumber = calendar.get(Calendar.MONTH) + 1,
                    dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH),
                    hour = time.first,
                    minute = time.second
                ),
                repetition = repetition
            )
        }

    internal suspend inline fun updateNote() = when (val note = initialNote) {
        is DatedNote -> updateDatedNote(note)
        is SimpleNote -> updateSimpleNote(note)
    }
}