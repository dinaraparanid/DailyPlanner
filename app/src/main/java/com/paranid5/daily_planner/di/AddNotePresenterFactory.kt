package com.paranid5.daily_planner.di

import androidx.lifecycle.MutableLiveData
import com.paranid5.daily_planner.data.note.Repetition
import com.paranid5.daily_planner.data.note.NoteType
import com.paranid5.daily_planner.presentation.add_note_dialog.AddNotePresenter
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory

@AssistedFactory
interface AddNotePresenterFactory {
    fun create(
        @Assisted("note_type") noteTypeState: MutableLiveData<NoteType>,
        @Assisted("title") titleInputState: MutableLiveData<String>,
        @Assisted("description") descriptionInputState: MutableLiveData<String>,
        @Assisted("repetition") repetitionState: MutableLiveData<Repetition>
    ): AddNotePresenter
}