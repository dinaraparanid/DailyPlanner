package com.paranid5.daily_planner.presentation.main_fragment

import androidx.lifecycle.MutableLiveData
import com.paranid5.daily_planner.data.note.Note
import com.paranid5.daily_planner.di.NotesState
import com.paranid5.daily_planner.presentation.BasePresenter
import javax.inject.Inject

class MainFragmentPresenter @Inject constructor(
    @NotesState val notesState: MutableLiveData<List<Note>>
) : BasePresenter