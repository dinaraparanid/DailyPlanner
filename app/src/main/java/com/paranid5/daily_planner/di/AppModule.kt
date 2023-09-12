package com.paranid5.daily_planner.di

import androidx.lifecycle.MutableLiveData
import com.paranid5.daily_planner.data.note.DatedNote
import com.paranid5.daily_planner.data.note.SimpleNote
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
    @SimpleNotesState
    fun provideSimpleNotesState() = MutableLiveData<List<SimpleNote>>(listOf())

    @Singleton
    @Provides
    @DatedNotesState
    fun provideDatedNotesState() = MutableLiveData<List<DatedNote>>(listOf())
}