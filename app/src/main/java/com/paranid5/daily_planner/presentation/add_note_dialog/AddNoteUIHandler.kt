package com.paranid5.daily_planner.presentation.add_note_dialog

import com.paranid5.daily_planner.data.Repetition
import com.paranid5.daily_planner.data.note.DatedNote
import com.paranid5.daily_planner.data.note.NoteType
import com.paranid5.daily_planner.data.note.SimpleNote
import com.paranid5.daily_planner.presentation.UIHandler
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random

@Singleton
class AddNoteUIHandler @Inject constructor() : UIHandler {
    fun setNoteType(viewModel: AddNoteViewModel, position: Int) {
        viewModel.noteType = NoteType.entries[position]
    }

    fun setRepetition(viewModel: AddNoteViewModel, position: Int) =
        viewModel.postRepetition(Repetition.fromIndex(position))

    fun addNote(viewModel: AddNoteViewModel) = viewModel.addNote(
        when (viewModel.noteType) {
            NoteType.SIMPLE -> SimpleNote(
                id = Random.nextInt(),
                title = viewModel.titleInput,
                description = viewModel.descriptionInput,
            )

            NoteType.DATED -> DatedNote(
                id = Random.nextInt(),
                title = viewModel.titleInput,
                description = viewModel.descriptionInput,
                // TODO: add date
                // TODO: add repetition
            )
        }
    )
}