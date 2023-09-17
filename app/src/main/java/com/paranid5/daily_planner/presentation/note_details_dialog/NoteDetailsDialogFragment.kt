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
import dagger.hilt.android.AndroidEntryPoint
import io.noties.markwon.Markwon
import io.noties.markwon.ext.latex.JLatexMathPlugin
import io.noties.markwon.ext.strikethrough.StrikethroughPlugin
import io.noties.markwon.ext.tables.TablePlugin
import io.noties.markwon.ext.tasklist.TaskListPlugin
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

    private val markwon by lazy {
        Markwon
            .builder(requireContext())
            .usePlugins(
                listOf(
                    StrikethroughPlugin.create(),
                    JLatexMathPlugin.create(13F),
                    TablePlugin.create(requireContext()),
                    TaskListPlugin.create(requireContext()),
                )
            )
            .build()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val binding = DataBindingUtil.inflate<DialogNoteDetailsBinding>(
            layoutInflater,
            R.layout.dialog_note_details,
            null,
            false
        )

        binding.viewModel = viewModel
        markwon.setMarkdown(binding.description, viewModel.note.description)

        return AlertDialog.Builder(requireContext())
            .setCancelable(true)
            .setView(binding.root)
            .setNeutralButton(R.string.edit) { dialog, _ ->
                viewModel.handler.launchEditDialog(viewModel.note)
                dialog.dismiss()
            }
            .create()
    }
}