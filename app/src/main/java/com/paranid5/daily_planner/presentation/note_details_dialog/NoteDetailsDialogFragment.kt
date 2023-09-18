package com.paranid5.daily_planner.presentation.note_details_dialog

import android.app.Dialog
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.paranid5.daily_planner.R
import com.paranid5.daily_planner.data.note.Note
import com.paranid5.daily_planner.databinding.DialogNoteDetailsBinding
import com.paranid5.daily_planner.di.NoteDetailsViewModelFactory
import com.paranid5.daily_planner.presentation.utils.ext.DefaultMarkwon
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class NoteDetailsDialogFragment : DialogFragment() {
    companion object {
        val TAG = NoteDetailsViewModel::class.simpleName!!
        const val NOTE_ARG = "note"

        fun newInstance(note: Note) = NoteDetailsDialogFragment().apply {
            arguments = Bundle().apply { putParcelable(NOTE_ARG, note) }
        }
    }

    @Inject
    lateinit var viewModelAssistedFactory: NoteDetailsViewModelFactory

    private val viewModel: NoteDetailsViewModel by viewModels {
        NoteDetailsViewModel.provideFactory(
            assistedFactory = viewModelAssistedFactory,
            note = when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU ->
                    requireArguments().getParcelable(NOTE_ARG, Note::class.java)!!

                else -> requireArguments().getParcelable(NOTE_ARG)!!
            }
        )
    }

    private val markwon by lazy { DefaultMarkwon(requireContext()) }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val binding = DataBindingUtil.inflate<DialogNoteDetailsBinding>(
            layoutInflater,
            R.layout.dialog_note_details,
            null,
            false
        ).apply {
            viewModel = this@NoteDetailsDialogFragment.viewModel

            markwon.setMarkdown(
                description,
                this@NoteDetailsDialogFragment.viewModel.note.description
            )
        }

        return AlertDialog.Builder(requireContext())
            .setCancelable(true)
            .setView(binding.root)
            .setNeutralButton(R.string.edit) { dialog, _ ->
                viewModel.handler.launchEditDialog(viewModel.note, parentFragmentManager)
                dialog.dismiss()
            }
            .create()
    }
}