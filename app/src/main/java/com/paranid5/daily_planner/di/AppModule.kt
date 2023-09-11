package com.paranid5.daily_planner.di

import androidx.lifecycle.MutableLiveData
import com.paranid5.daily_planner.data.Note
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Singleton
    @Provides
    @NotesState
    fun provideNotesState() = MutableLiveData<List<Note>>(listOf())
}