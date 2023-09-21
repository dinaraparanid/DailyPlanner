package com.paranid5.daily_planner

import android.app.AlarmManager
import android.app.Application
import androidx.core.content.getSystemService
import com.paranid5.daily_planner.data.note.DatedNote
import com.paranid5.daily_planner.data.room.notes.NotesRepository
import com.paranid5.daily_planner.domain.utils.ext.launchNoteAlarm
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class MainApplication : Application(), CoroutineScope by MainScope() {
    @Inject
    lateinit var notesRepository: NotesRepository

    private val alarmManager by lazy {
        getSystemService<AlarmManager>()!!
    }

    override fun onCreate() {
        super.onCreate()
        launch(Dispatchers.IO) { launchNotesAlarms() }
    }

    private suspend inline fun launchNotesAlarms() = coroutineScope {
        notesRepository.getDatedNotes().forEach {
            alarmManager.launchNoteAlarm(applicationContext, DatedNote(it))
        }
    }
}