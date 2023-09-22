package com.paranid5.daily_planner.domain.utils.ext

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.paranid5.daily_planner.data.note.DatedNote
import com.paranid5.daily_planner.domain.AlarmReceiver

@SuppressLint("ScheduleExactAlarm")
fun AlarmManager.launchNoteAlarm(
    context: Context,
    note: DatedNote,
) = when {
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ->
        setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            note.date.epochMillis,
            PendingIntent.getBroadcast(
                context.applicationContext,
                note.hashCode(),
                Intent(context, AlarmReceiver::class.java).apply {
                    action = AlarmReceiver.Broadcast_ALARM_RECEIVED
                    putExtra(AlarmReceiver.NOTE_ARG, note)
                },
                PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
            )
        )

    else -> setExact(
        AlarmManager.RTC_WAKEUP,
        note.date.epochMillis,
        PendingIntent.getBroadcast(
            context.applicationContext,
            note.hashCode(),
            Intent(context, AlarmReceiver::class.java).apply {
                action = AlarmReceiver.Broadcast_ALARM_RECEIVED
                putExtra(AlarmReceiver.NOTE_ARG, note)
            },
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )
    )
}

fun AlarmManager.cancelNoteAlarm(
    context: Context,
    note: DatedNote,
) = PendingIntent.getBroadcast(
    context.applicationContext,
    note.hashCode(),
    Intent(context, AlarmReceiver::class.java).apply {
        action = AlarmReceiver.Broadcast_ALARM_RECEIVED
    },
    PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
)?.let(this::cancel)