package com.paranid5.daily_planner.presentation.add_note_dialog

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import com.paranid5.daily_planner.R
import com.paranid5.daily_planner.data.note.NoteType
import com.paranid5.daily_planner.databinding.DialogAddNoteBinding
import dagger.hilt.android.AndroidEntryPoint
import io.noties.markwon.Markwon
import io.noties.markwon.editor.MarkwonEditor
import io.noties.markwon.editor.MarkwonEditorTextWatcher
import io.noties.markwon.ext.latex.JLatexMathPlugin
import io.noties.markwon.ext.strikethrough.StrikethroughPlugin
import io.noties.markwon.ext.tables.TablePlugin
import io.noties.markwon.ext.tasklist.TaskListPlugin
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.Executors

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

    private inline val typeSpinnerAdapter
        get() = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.note_types,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

    private inline val typeSpinnerListener
        get() = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) =
                mSetNoteType(position)

            override fun onNothingSelected(parent: AdapterView<*>?) = Unit
        }

    private inline val repetitionAdapter
        get() = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.repetition_type,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

    private inline val repetitionListener
        get() = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) =
                mSetRepetition(position)

            override fun onNothingSelected(parent: AdapterView<*>?) = Unit
        }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val binding = DataBindingUtil.inflate<DialogAddNoteBinding>(
            layoutInflater,
            R.layout.dialog_add_note,
            null,
            false
        )

        binding.viewModel = viewModel
        binding.typeSpinner.initTypeSpinner()
        binding.repetitionSpinner.initRepetitionSpinner()
        binding.descriptionInput.initMarkwonEditor()

        return AlertDialog.Builder(requireContext())
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

    internal fun mSetNoteType(position: Int) =
        viewModel.handler.setNoteType(viewModel, position)

    internal fun mSetRepetition(position: Int) =
        viewModel.handler.setRepetition(viewModel, position)

    private fun Spinner.initTypeSpinner() {
        adapter = typeSpinnerAdapter
        onItemSelectedListener = typeSpinnerListener
        setSelection(requireArguments().getInt(NOTES_TYPE_ARG))
    }

    private fun Spinner.initRepetitionSpinner() {
        adapter = repetitionAdapter
        onItemSelectedListener = repetitionListener
    }

    private fun EditText.initMarkwonEditor() {
        val markwon = Markwon
            .builder(requireContext())
            .usePlugins(
                listOf(
                    StrikethroughPlugin.create(),
                    JLatexMathPlugin.create(13F),
                    TablePlugin.create(context),
                    TaskListPlugin.create(context)
                )
            )
            .build()

        val editor = MarkwonEditor.create(markwon)

        addTextChangedListener(
            MarkwonEditorTextWatcher.withPreRender(
                editor,
                Executors.newCachedThreadPool(),
                this
            )
        )
    }
}