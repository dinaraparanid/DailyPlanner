package com.paranid5.daily_planner.presentation.edit_note_dialog

import com.paranid5.daily_planner.data.note.Repetition
import com.paranid5.daily_planner.presentation.UIHandler
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EditNoteUIHandler @Inject constructor() : UIHandler {
    fun setRepetition(viewModel: EditNoteViewModel, position: Int) =
        viewModel.postRepetition(Repetition.fromOrdinal(position))

    internal suspend inline fun updateNote(viewModel: EditNoteViewModel) = viewModel.updateNote()
}