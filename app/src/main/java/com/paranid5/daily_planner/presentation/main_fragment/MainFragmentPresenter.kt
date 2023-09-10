package com.paranid5.daily_planner.presentation.main_fragment

import androidx.lifecycle.MutableLiveData
import com.paranid5.daily_planner.data.Note
import com.paranid5.daily_planner.presentation.BasePresenter
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

class MainFragmentPresenter @AssistedInject constructor(
    @Assisted val notesState: MutableLiveData<List<Note>>
) : BasePresenter