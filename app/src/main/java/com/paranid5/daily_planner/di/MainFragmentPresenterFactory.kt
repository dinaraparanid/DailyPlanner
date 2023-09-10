package com.paranid5.daily_planner.di

import androidx.lifecycle.MutableLiveData
import com.paranid5.daily_planner.data.Note
import com.paranid5.daily_planner.presentation.main_fragment.MainFragmentPresenter
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory

@AssistedFactory
interface MainFragmentPresenterFactory {
    fun create(@Assisted notesState: MutableLiveData<List<Note>>): MainFragmentPresenter
}