package com.paranid5.daily_planner.presentation.add_note_dialog

import com.paranid5.daily_planner.data.Note
import com.paranid5.daily_planner.presentation.UIHandler
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random

@Singleton
class AddNoteUIHandler @Inject constructor() : UIHandler {
    fun addNote(viewModel: AddNoteViewModel) = viewModel.addNote(
        Note(
            id = Random.nextInt(),
            title = viewModel.titleInput,
            description = viewModel.descriptionInput,
        )
    )
}