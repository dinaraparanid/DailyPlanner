package com.paranid5.daily_planner.presentation.notes_fragments.simple_notes

import androidx.lifecycle.MutableLiveData
import com.paranid5.daily_planner.data.note.SimpleNote
import com.paranid5.daily_planner.di.SimpleNotesState
import com.paranid5.daily_planner.presentation.BasePresenter
import javax.inject.Inject

class SimpleNotesPresenter @Inject constructor(
    @SimpleNotesState val notesState: MutableLiveData<List<SimpleNote>>
) : BasePresenter