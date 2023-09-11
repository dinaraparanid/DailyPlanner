package com.paranid5.daily_planner.presentation.add_note_dialog

import androidx.lifecycle.MutableLiveData
import com.paranid5.daily_planner.data.Note
import com.paranid5.daily_planner.di.NotesState
import com.paranid5.daily_planner.presentation.BasePresenter
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

class AddNotePresenter @AssistedInject constructor(
    @NotesState val notesState: MutableLiveData<List<Note>>,
    @Assisted("title") val titleInputState: MutableLiveData<String>,
    @Assisted("description") val descriptionInputState: MutableLiveData<String>
) : BasePresenter