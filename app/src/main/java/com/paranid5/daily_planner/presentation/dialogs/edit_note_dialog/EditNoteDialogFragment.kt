package com.paranid5.daily_planner.presentation.dialogs.edit_note_dialog

import android.app.Dialog
import android.content.DialogInterface
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
import com.paranid5.daily_planner.presentation.UIStateChangesObserver
import com.paranid5.daily_planner.presentation.utils.DatePicker
import com.paranid5.daily_planner.presentation.utils.TimePicker
import com.paranid5.daily_planner.presentation.utils.ext.initMarkwonEditor
import com.paranid5.daily_planner.presentation.utils.ext.initRepetitionSpinner
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class EditNoteDialogFragment : DialogFragment(), UIStateChangesObserver {
    companion object {
        val TAG = EditNoteDialogFragment::class.simpleName!!
        const val INITIAL_NOTE_ARG = "note"

        private const val DATE_PICKER_TAG = "NoteDatePicker"
        private const val TIME_PICKER_TAG = "NoteTimePicker"

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
            .setCancelable(false)
            .setView(binding.root)
            .setPositiveButton(R.string.ok) { dialog, _ ->
                viewModel.viewModelScope.launch(Dispatchers.IO) {
                    viewModel.handler.updateNote(requireContext(), viewModel)
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
        DataBindingUtil.inflate<DialogEditNoteBinding>(
            layoutInflater,
            R.layout.dialog_edit_note,
            null,
            false
        )

    private fun DialogEditNoteBinding.initView() {
        val vm = this@EditNoteDialogFragment.viewModel
        viewModel = vm

        titleInput.setText(vm.titleInput)

        repetitionSpinner.initRepetitionSpinner(
            context = requireContext(),
            setRepetition = this@EditNoteDialogFragment::setRepetition,
            initialRepetitionOrdinal = vm.repetition.ordinal
        )

        datePickerLauncher.setOnClickListener {
            datePicker.show(parentFragmentManager, DATE_PICKER_TAG)
        }

        timePickerLauncher.setOnClickListener {
            timePicker.show(parentFragmentManager, TIME_PICKER_TAG)
        }

        descriptionInput.setText(vm.descriptionInput)
        descriptionInput.initMarkwonEditor(requireContext())
    }

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