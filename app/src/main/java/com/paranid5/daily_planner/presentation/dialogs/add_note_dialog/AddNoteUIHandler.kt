package com.paranid5.daily_planner.presentation.dialogs.add_note_dialog

import android.app.AlarmManager
import android.content.Context
import com.paranid5.daily_planner.data.note.DatedNote
import com.paranid5.daily_planner.data.note.NoteType
import com.paranid5.daily_planner.data.note.Repetition
import com.paranid5.daily_planner.data.note.SimpleNote
import com.paranid5.daily_planner.domain.utils.ext.launchNoteAlarm
import com.paranid5.daily_planner.presentation.UIHandler
import kotlinx.datetime.LocalDateTime
import java.util.Calendar
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AddNoteUIHandler @Inject constructor() : UIHandler {
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
                val calendar = Calendar
                    .getInstance()
                    .apply { time = Date(viewModel.date) }

                val note = DatedNote(
                    title = viewModel.titleInput,
                    description = viewModel.descriptionInput,
                    date = LocalDateTime(
                        year = calendar.get(Calendar.YEAR),
                        monthNumber = calendar.get(Calendar.MONTH) + 1,
                        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH),
                        hour = viewModel.time.first,
                        minute = viewModel.time.second
                    ),
                    repetition = viewModel.repetition
                )

                alarmManager.launchNoteAlarm(context, note.copy(id = viewModel.addNote(note)))
            }
        }
    }
}