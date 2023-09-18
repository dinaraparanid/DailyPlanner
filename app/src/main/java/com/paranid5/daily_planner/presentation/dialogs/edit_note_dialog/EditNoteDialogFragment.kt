package com.paranid5.daily_planner.presentation.dialogs.edit_note_dialog

import android.app.Dialog
import android.os.Build
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.paranid5.daily_planner.R
import com.paranid5.daily_planner.data.note.Note
import com.paranid5.daily_planner.data.note.ordinal
import com.paranid5.daily_planner.databinding.DialogEditNoteBinding
import com.paranid5.daily_planner.di.EditNoteViewModelFactory
import com.paranid5.daily_planner.presentation.utils.ext.initMarkwonEditor
import com.paranid5.daily_planner.presentation.utils.ext.initRepetitionSpinner
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class EditNoteDialogFragment : DialogFragment() {
    companion object {
        val TAG = EditNoteDialogFragment::class.simpleName!!
        const val INITIAL_NOTE_ARG = "note"

        fun newInstance(initialNote: Note) = EditNoteDialogFragment().apply {
            arguments = Bundle().apply { putParcelable(INITIAL_NOTE_ARG, initialNote) }
        }
    }

    @Inject
    lateinit var viewModelAssistedFactory: EditNoteViewModelFactory

    private val viewModel: EditNoteViewModel by viewModels {
        EditNoteViewModel.provideFactory(
            assistedFactory = viewModelAssistedFactory,
            initialNote = when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU ->
                    requireArguments().getParcelable(INITIAL_NOTE_ARG, Note::class.java)!!

                else -> requireArguments().getParcelable(INITIAL_NOTE_ARG)!!
            }
        )
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val binding = DataBindingUtil.inflate<DialogEditNoteBinding>(
            layoutInflater,
            R.layout.dialog_edit_note,
            null,
            false
        ).apply {
            val vm = this@EditNoteDialogFragment.viewModel
            viewModel = vm

            repetitionSpinner.initRepetitionSpinner(
                context = requireContext(),
                setRepetition = this@EditNoteDialogFragment::setRepetition,
                initialRepetitionOrdinal = vm.repetition.ordinal
            )

            titleInput.setText(vm.titleInput)
            descriptionInput.setText(vm.descriptionInput)
            descriptionInput.initMarkwonEditor(requireContext())
        }

        return MaterialAlertDialogBuilder(requireContext())
            .setCancelable(false)
            .setView(binding.root)
            .setPositiveButton(R.string.ok) { dialog, _ ->
                viewModel.viewModelScope.launch(Dispatchers.IO) {
                    viewModel.handler.updateNote(viewModel)
                }

                dialog.dismiss()
            }
            .create()
    }

    private fun setRepetition(position: Int) =
        viewModel.handler.setRepetition(viewModel, position)
}