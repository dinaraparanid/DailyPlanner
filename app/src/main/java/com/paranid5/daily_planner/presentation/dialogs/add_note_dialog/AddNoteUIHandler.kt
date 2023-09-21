package com.paranid5.daily_planner.presentation.dialogs.add_note_dialog

import android.app.AlarmManager
import android.content.Context
import android.widget.Toast
import com.paranid5.daily_planner.R
import com.paranid5.daily_planner.data.note.DatedNote
import com.paranid5.daily_planner.data.note.NoteType
import com.paranid5.daily_planner.data.note.Repetition
import com.paranid5.daily_planner.data.note.SimpleNote
import com.paranid5.daily_planner.domain.utils.ext.launchNoteAlarm
import com.paranid5.daily_planner.presentation.UIHandler
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import java.util.Calendar
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AddNoteUIHandler @Inject constructor() : UIHandler {
    private val calendar by lazy {
        Calendar.getInstance()
    }

    fun setNoteType(viewModel: AddNoteViewModel, position: Int) {
        viewModel.noteType = NoteType.entries[position]
    }

    fun setRepetition(viewModel: AddNoteViewModel, position: Int) =
        viewModel.postRepetition(Repetition.fromOrdinal(position))

    internal suspend inline fun addNote(
        context: Context,
        viewModel: AddNoteViewModel,
        alarmManager: AlarmManager
    ) {
        when (viewModel.noteType) {
            NoteType.SIMPLE -> viewModel.addNote(
                SimpleNote(
                    title = viewModel.titleInput,
                    description = viewModel.descriptionInput,
                )
            )

            NoteType.DATED -> {
                calendar.time = Date(viewModel.date)

                val date = LocalDateTime(
                    year = calendar.get(Calendar.YEAR),
                    monthNumber = calendar.get(Calendar.MONTH) + 1,
                    dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH),
                    hour = viewModel.time.first,
                    minute = viewModel.time.second
                )

                if (date.toInstant(TimeZone.currentSystemDefault()) <= Clock.System.now()) {
                    Toast.makeText(context, R.string.incorrect_time, Toast.LENGTH_LONG).show()
                    return
                }

                val note = DatedNote(
                    title = viewModel.titleInput,
                    description = viewModel.descriptionInput,
                    date = date,
                    repetition = viewModel.repetition
                )

                alarmManager.launchNoteAlarm(context, note.copy(id = viewModel.addNote(note)))
            }
        }
    }
}