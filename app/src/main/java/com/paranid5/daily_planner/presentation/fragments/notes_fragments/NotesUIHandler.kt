package com.paranid5.daily_planner.presentation.fragments.notes_fragments

import androidx.fragment.app.FragmentManager
import com.paranid5.daily_planner.data.note.NoteType
import com.paranid5.daily_planner.presentation.UIHandler
import com.paranid5.daily_planner.presentation.dialogs.add_note_dialog.AddNoteDialogFragment

interface NotesUIHandler : UIHandler {
    val notesType: NoteType

    fun onAddNoteButtonClicked(fragmentManager: FragmentManager) =
        AddNoteDialogFragment
            .newInstance(notesType)
            .show(fragmentManager, AddNoteDialogFragment.TAG)
}