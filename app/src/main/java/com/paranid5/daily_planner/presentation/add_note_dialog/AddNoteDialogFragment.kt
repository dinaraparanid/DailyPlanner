package com.paranid5.daily_planner.presentation.add_note_dialog

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.paranid5.daily_planner.R
import com.paranid5.daily_planner.databinding.DialogAddNoteBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddNoteDialogFragment : DialogFragment() {
    companion object {
        val TAG = AddNoteDialogFragment::class.simpleName!!
    }

    private val viewModel by viewModels<AddNoteViewModel>()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val binding = DataBindingUtil.inflate<DialogAddNoteBinding>(
            layoutInflater,
            R.layout.dialog_add_note,
            null,
            false
        )

        binding.viewModel = viewModel

        return AlertDialog.Builder(requireContext())
            .setCancelable(true)
            .setView(binding.root)
            .setPositiveButton(R.string.ok) { dialog, _ ->
                viewModel.handler.addNote(viewModel)
                dialog.dismiss()
            }
            .create()
    }
}