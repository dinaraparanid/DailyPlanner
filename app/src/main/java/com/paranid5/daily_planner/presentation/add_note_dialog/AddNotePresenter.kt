package com.paranid5.daily_planner.presentation.add_note_dialog

import android.view.View
import androidx.annotation.MainThread
import androidx.databinding.Bindable
import androidx.lifecycle.MutableLiveData
import com.paranid5.daily_planner.BR
import com.paranid5.daily_planner.data.Repetition
import com.paranid5.daily_planner.data.note.DatedNote
import com.paranid5.daily_planner.data.note.NoteType
import com.paranid5.daily_planner.data.note.SimpleNote
import com.paranid5.daily_planner.di.DatedNotesState
import com.paranid5.daily_planner.di.SimpleNotesState
import com.paranid5.daily_planner.presentation.ObservablePresenter
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

class AddNotePresenter @AssistedInject constructor(
    @SimpleNotesState val simpleNotesState: MutableLiveData<List<SimpleNote>>,
    @DatedNotesState val datedNotesState: MutableLiveData<List<DatedNote>>,
    @Assisted("note_type") val noteTypeState: MutableLiveData<NoteType>,
    @Assisted("title") val titleInputState: MutableLiveData<String>,
    @Assisted("description") val descriptionInputState: MutableLiveData<String>,
    @Assisted("repetition") val repetitionState: MutableLiveData<Repetition>
) : ObservablePresenter() {
    inline var noteType
        get() = noteTypeState.value!!

        @MainThread
        set(value) {
            noteTypeState.value = value
            notifyPropertyChanged(BR.dateElementsVisibility)
        }

    @get:Bindable
    val dateElementsVisibility
        @JvmName("getDateElementsVisibility")
        get() = if (noteType == NoteType.DATED) View.VISIBLE else View.GONE
}