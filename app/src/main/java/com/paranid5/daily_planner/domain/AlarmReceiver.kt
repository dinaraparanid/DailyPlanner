package com.paranid5.daily_planner.domain

import android.app.AlarmManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast
import androidx.core.content.getSystemService
import com.paranid5.daily_planner.data.note.DatedNote
import com.paranid5.daily_planner.data.room.notes.NotesRepository
import com.paranid5.daily_planner.data.utils.ext.nextAlarmTime
import com.paranid5.daily_planner.domain.utils.ext.launchNoteAlarm
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AlarmReceiver : BroadcastReceiver(), CoroutineScope by CoroutineScope(Dispatchers.IO) {
    @Inject
    lateinit var notesRepository: NotesRepository

    companion object {
        const val Broadcast_ALARM_RECEIVED = "com.paranid5.daily_planner.domain.ALARM_RECEIVED"
        const val NOTE_ARG = "note"
    }

    override fun onReceive(context: Context, intent: Intent) {
        val alarmManager = context.getSystemService<AlarmManager>()!!

        val note = when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU ->
                intent.getParcelableExtra(NOTE_ARG, DatedNote::class.java)
            else -> intent.getParcelableExtra(NOTE_ARG)
        } ?: return

        // TODO: notification
        Toast.makeText(context, note.title, Toast.LENGTH_LONG).show()

        note.nextAlarmTime?.let { newTime ->
            val newNote = note.copy(date = newTime)
            launch(Dispatchers.IO) { notesRepository.update(newNote) }
            alarmManager.launchNoteAlarm(context, newNote)
        }
    }
}