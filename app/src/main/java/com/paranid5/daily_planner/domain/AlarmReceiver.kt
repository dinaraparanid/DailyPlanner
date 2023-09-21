package com.paranid5.daily_planner.domain

import android.app.AlarmManager
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.content.getSystemService
import com.paranid5.daily_planner.R
import com.paranid5.daily_planner.data.note.DatedNote
import com.paranid5.daily_planner.data.room.notes.NotesRepository
import com.paranid5.daily_planner.data.utils.ext.nextAlarmTime
import com.paranid5.daily_planner.domain.utils.ext.launchNoteAlarm
import com.paranid5.daily_planner.presentation.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AlarmReceiver : BroadcastReceiver(), CoroutineScope by CoroutineScope(Dispatchers.IO) {
    companion object {
        private const val NOTIFICATION_ID = 100
        private const val ALARM_CHANNEL_ID = "alarm_channel"

        const val Broadcast_ALARM_RECEIVED = "com.paranid5.daily_planner.domain.ALARM_RECEIVED"
        const val NOTE_ARG = "note"
    }

    @Inject
    lateinit var notesRepository: NotesRepository

    @Volatile
    private var isNotificationChannelCreated = false

    private lateinit var notificationManager: NotificationManager

    override fun onReceive(context: Context, intent: Intent) {
        notificationManager = context.getSystemService<NotificationManager>()!!
        val alarmManager = context.getSystemService<AlarmManager>()!!

        val note = when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU ->
                intent.getParcelableExtra(NOTE_ARG, DatedNote::class.java)
            else -> intent.getParcelableExtra(NOTE_ARG)
        } ?: return

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            createNotificationChannel()

        showNotification(context, note)

        note.nextAlarmTime?.let { newTime ->
            val newNote = note.copy(date = newTime, isDone = false)
            launch(Dispatchers.IO) { notesRepository.update(newNote) }
            alarmManager.launchNoteAlarm(context, newNote)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        if (isNotificationChannelCreated) return

        notificationManager.createNotificationChannel(
            NotificationChannel(
                ALARM_CHANNEL_ID,
                "Note Alarm",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                lockscreenVisibility = Notification.VISIBILITY_PUBLIC
                enableVibration(true)
                enableLights(true)
            }
        )

        isNotificationChannelCreated = true
    }

    private fun NotificationBuilder(context: Context, note: DatedNote) =
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ->
                Notification.Builder(context.applicationContext, ALARM_CHANNEL_ID)
            else -> Notification.Builder(context.applicationContext)
        }
            .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
            .setContentTitle(note.title)
            .setContentText(context.resources.getString(R.string.time_s_app))
            .setOngoing(false)
            .setShowWhen(true)
            .setOnlyAlertOnce(true)
            .setContentIntent(
                PendingIntent.getActivity(
                    context.applicationContext,
                    note.hashCode(),
                    Intent(context.applicationContext, MainActivity::class.java),
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
            )

    private fun showNotification(context: Context, note: DatedNote) =
        notificationManager.notify(NOTIFICATION_ID, NotificationBuilder(context, note).build())
}