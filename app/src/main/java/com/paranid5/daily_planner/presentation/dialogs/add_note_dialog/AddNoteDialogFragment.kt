package com.paranid5.daily_planner.presentation.dialogs.add_note_dialog

import android.app.Dialog
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.paranid5.daily_planner.R
import com.paranid5.daily_planner.data.note.NoteType
import com.paranid5.daily_planner.data.note.Repetition
import com.paranid5.daily_planner.data.note.ordinal
import com.paranid5.daily_planner.databinding.DialogAddNoteBinding
import com.paranid5.daily_planner.presentation.utils.ext.initMarkwonEditor
import com.paranid5.daily_planner.presentation.utils.ext.initRepetitionSpinner
import com.paranid5.daily_planner.presentation.utils.ext.initTypeSpinner
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddNoteDialogFragment : DialogFragment() {
    companion object {
        val TAG = AddNoteDialogFragment::class.simpleName!!
        const val NOTES_TYPE_ARG = "notes_type"

        fun newInstance(noteType: NoteType) = AddNoteDialogFragment().apply {
            arguments = Bundle().apply { putInt(NOTES_TYPE_ARG, noteType.ordinal) }
        }
    }

    private val viewModel by viewModels<AddNoteViewModel>()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val binding = DataBindingUtil.inflate<DialogAddNoteBinding>(
            layoutInflater,
            R.layout.dialog_add_note,
            null,
            false
        ).apply {
            viewModel = this@AddNoteDialogFragment.viewModel

            typeSpinner.initTypeSpinner(
                context = requireContext(),
                initialNoteType = requireArguments().getInt(NOTES_TYPE_ARG),
                setNoteType = this@AddNoteDialogFragment::setNoteType
            )

            repetitionSpinner.initRepetitionSpinner(
                context = requireContext(),
                setRepetition = this@AddNoteDialogFragment::setRepetition,
                initialRepetitionOrdinal = Repetition.NoRepetition.ordinal
            )

            descriptionInput.initMarkwonEditor(requireContext())
        }

        return MaterialAlertDialogBuilder(requireContext())
            .setCancelable(true)
            .setView(binding.root)
            .setPositiveButton(R.string.ok) { dialog, _ ->
                viewModel.viewModelScope.launch(Dispatchers.IO) {
                    viewModel.handler.addNote(viewModel)
                }

                dialog.dismiss()
            }
            .create()
    }

    private fun setNoteType(position: Int) =
        viewModel.handler.setNoteType(viewModel, position)

    private fun setRepetition(position: Int) =
        viewModel.handler.setRepetition(viewModel, position)
}