package com.paranid5.daily_planner.presentation.main_fragment

import androidx.fragment.app.FragmentManager
import com.paranid5.daily_planner.presentation.UIHandler
import com.paranid5.daily_planner.presentation.add_note_dialog.AddNoteDialogFragment
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainFragmentUIHandler @Inject constructor() : UIHandler {
    fun onAddNoteButtonClicked(fragmentManager: FragmentManager) =
        AddNoteDialogFragment().show(fragmentManager, AddNoteDialogFragment.TAG)
}