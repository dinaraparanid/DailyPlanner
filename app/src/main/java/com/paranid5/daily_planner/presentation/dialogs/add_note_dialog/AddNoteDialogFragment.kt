package com.paranid5.daily_planner.presentation.dialogs.add_note_dialog

import android.app.Dialog
import android.content.DialogInterface
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
import com.paranid5.daily_planner.presentation.UIStateChangesObserver
import com.paranid5.daily_planner.presentation.utils.DatePicker
import com.paranid5.daily_planner.presentation.utils.TimePicker
import com.paranid5.daily_planner.presentation.utils.ext.initMarkwonEditor
import com.paranid5.daily_planner.presentation.utils.ext.initRepetitionSpinner
import com.paranid5.daily_planner.presentation.utils.ext.initTypeSpinner
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddNoteDialogFragment : DialogFragment(), UIStateChangesObserver {
    companion object {
        val TAG = AddNoteDialogFragment::class.simpleName!!
        const val NOTES_TYPE_ARG = "notes_type"

        private const val DATE_PICKER_TAG = "NoteDatePicker"
        private const val TIME_PICKER_TAG = "NoteTimePicker"

        fun newInstance(noteType: NoteType) = AddNoteDialogFragment().apply {
            arguments = Bundle().apply { putInt(NOTES_TYPE_ARG, noteType.ordinal) }
        }
    }

    private val viewModel by viewModels<AddNoteViewModel>()

    private val datePicker by lazy {
        DatePicker(viewModel::postDate)
    }

    private val timePicker by lazy {
        TimePicker(viewModel::postTime)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val binding = inflateViewBinding().apply { initView() }
        observeUIStateChanges()

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

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        removeUIStatesObservers()
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        removeUIStatesObservers()
    }

    private fun inflateViewBinding() =
        DataBindingUtil.inflate<DialogAddNoteBinding>(
            layoutInflater,
            R.layout.dialog_add_note,
            null,
            false
        )

    private fun DialogAddNoteBinding.initView() {
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

        datePickerLauncher.setOnClickListener {
            datePicker.show(parentFragmentManager, DATE_PICKER_TAG)
        }

        timePickerLauncher.setOnClickListener {
            timePicker.show(parentFragmentManager, TIME_PICKER_TAG)
        }

        descriptionInput.initMarkwonEditor(requireContext())
    }

    private fun setNoteType(position: Int) =
        viewModel.handler.setNoteType(viewModel, position)

    private fun setRepetition(position: Int) =
        viewModel.handler.setRepetition(viewModel, position)

    override fun observeUIStateChanges() {
        viewModel.dateState.observe(requireActivity()) { viewModel.presenter.notifyDateChanged() }
        viewModel.timeState.observe(requireActivity()) { viewModel.presenter.notifyTimeChanged() }
    }

    private fun removeUIStatesObservers() {
        viewModel.dateState.removeObservers(requireActivity())
        viewModel.timeState.removeObservers(requireActivity())
    }
}