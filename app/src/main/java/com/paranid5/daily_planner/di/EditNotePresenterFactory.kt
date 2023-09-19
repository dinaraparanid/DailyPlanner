package com.paranid5.daily_planner.di

import androidx.lifecycle.MutableLiveData
import com.paranid5.daily_planner.data.note.Note
import com.paranid5.daily_planner.data.note.Repetition
import com.paranid5.daily_planner.presentation.dialogs.edit_note_dialog.EditNotePresenter
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory

@AssistedFactory
interface EditNotePresenterFactory {
    fun create(
        @Assisted("note") initialNote: Note,
        @Assisted("title") titleInputState: MutableLiveData<String>,
        @Assisted("description") descriptionInputState: MutableLiveData<String>,
        @Assisted("date") dateState: MutableLiveData<Long?>,
        @Assisted("time") timeState: MutableLiveData<Pair<Int, Int>?>,
        @Assisted("repetition") repetitionState: MutableLiveData<Repetition>
    ): EditNotePresenter
}