package com.paranid5.daily_planner.presentation.dialogs.note_details_dialog

import androidx.fragment.app.FragmentManager
import com.paranid5.daily_planner.data.note.Note
import com.paranid5.daily_planner.presentation.UIHandler
import com.paranid5.daily_planner.presentation.dialogs.edit_note_dialog.EditNoteDialogFragment
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NoteDetailsUIHandler @Inject constructor() : UIHandler {
    fun launchEditDialog(note: Note, fragmentManager: FragmentManager) =
        EditNoteDialogFragment
            .newInstance(note)
            .show(fragmentManager, EditNoteDialogFragment.TAG)
}