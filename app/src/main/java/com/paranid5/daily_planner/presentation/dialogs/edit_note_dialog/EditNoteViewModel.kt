package com.paranid5.daily_planner.presentation.dialogs.edit_note_dialog

import android.app.AlarmManager
import android.content.Context
import android.widget.Toast
import androidx.core.content.getSystemService
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import com.paranid5.daily_planner.R
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
import com.paranid5.daily_planner.domain.utils.ext.cancelNoteAlarm
import com.paranid5.daily_planner.domain.utils.ext.launchNoteAlarm
import com.paranid5.daily_planner.presentation.ObservableViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import java.util.Calendar
import java.util.Date

class EditNoteViewModel @AssistedInject constructor(
    presenterFactory: EditNotePresenterFactory,
    override val handler: EditNoteUIHandler,
    private val notesRepository: NotesRepository,
    @ApplicationContext context: Context,
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

    private val alarmManager by lazy {
        context.getSystemService<AlarmManager>()!!
    }

    private val calendar by lazy {
        Calendar.getInstance()
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

    private suspend inline fun updateDatedNote(
        context: Context,
        note: DatedNote
    ) {
        alarmManager.cancelNoteAlarm(context, note)
        calendar.time = Date(date)

        val date = LocalDateTime(
            year = calendar.get(Calendar.YEAR),
            monthNumber = calendar.get(Calendar.MONTH) + 1,
            dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH),
            hour = time.first,
            minute = time.second
        )

        if (date.toInstant(TimeZone.currentSystemDefault()) <= Clock.System.now()) {
            Toast.makeText(context, R.string.incorrect_time, Toast.LENGTH_LONG).show()
            return
        }

        val newNote = note.copy(
            title = titleInput,
            description = descriptionInput,
            date = date,
            repetition = repetition
        )

        alarmManager.launchNoteAlarm(context, newNote)
        notesRepository.update(newNote)
    }

    internal suspend inline fun updateNote(context: Context) = when (val note = initialNote) {
        is DatedNote -> updateDatedNote(context, note)
        is SimpleNote -> updateSimpleNote(note)
    }
}