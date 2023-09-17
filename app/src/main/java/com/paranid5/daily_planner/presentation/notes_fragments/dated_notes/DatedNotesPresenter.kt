package com.paranid5.daily_planner.presentation.notes_fragments.dated_notes

import androidx.lifecycle.MutableLiveData
import com.paranid5.daily_planner.data.note.DatedNote
import com.paranid5.daily_planner.di.DatedNotesState
import com.paranid5.daily_planner.presentation.BasePresenter
import javax.inject.Inject

data class DatedNotesPresenter @Inject constructor(
    @DatedNotesState val notesState: MutableLiveData<List<DatedNote>>
) : BasePresenter