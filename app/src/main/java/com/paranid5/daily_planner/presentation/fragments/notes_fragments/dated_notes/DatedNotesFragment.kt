package com.paranid5.daily_planner.presentation.fragments.notes_fragments.dated_notes

import android.app.AlarmManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.getSystemService
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.paranid5.daily_planner.R
import com.paranid5.daily_planner.data.note.DatedNote
import com.paranid5.daily_planner.data.note.Note
import com.paranid5.daily_planner.data.room.notes.NotesRepository
import com.paranid5.daily_planner.databinding.FragmentNotesBinding
import com.paranid5.daily_planner.di.DatedNotesState
import com.paranid5.daily_planner.presentation.UIStateChangesObserver
import com.paranid5.daily_planner.presentation.dialogs.AlarmPermissionRequiredDialogFragment
import com.paranid5.daily_planner.presentation.dialogs.note_details_dialog.NoteDetailsDialogFragment
import com.paranid5.daily_planner.presentation.fragments.notes_fragments.NotesAdapter
import com.paranid5.daily_planner.presentation.fragments.notes_fragments.NotesTouchHandler
import com.paranid5.daily_planner.presentation.utils.decorations.VerticalSpaceItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DatedNotesFragment : Fragment(), UIStateChangesObserver {
    @Inject
    lateinit var notesRepository: NotesRepository

    @Inject
    @DatedNotesState
    lateinit var datedNotesState: MutableLiveData<List<DatedNote>>

    private lateinit var binding: FragmentNotesBinding

    private val viewModel by viewModels<DatedNotesViewModel>()

    private val adapter by lazy {
        NotesAdapter(requireContext(), notesRepository) {
            NoteDetailsDialogFragment
                .newInstance(it)
                .show(childFragmentManager, NoteDetailsDialogFragment.TAG)
        }
    }

    private val touchHelper by lazy {
        @Suppress("UNCHECKED_CAST")
        ItemTouchHelper(
            NotesTouchHandler(
                datedNotesState as LiveData<List<Note>>,
                notesRepository
            )
        )
    }

    private val alarmManager by lazy {
        requireContext().getSystemService<AlarmManager>()!!
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate<FragmentNotesBinding?>(
            inflater,
            R.layout.fragment_notes,
            container,
            false
        ).apply {
            addNoteFAB.setOnClickListener {
                fun addNote() =
                    viewModel.handler.onAddNoteButtonClicked(childFragmentManager)

                when {
                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.S ->
                        if (checkAndRequestAlarmPermission()) addNote()

                    else -> addNote()
                }
            }

            notesList.also {
                it.layoutManager = LinearLayoutManager(context)
                it.adapter = adapter
                it.addItemDecoration(VerticalSpaceItemDecoration(30))
                touchHelper.attachToRecyclerView(it)
            }
        }

        observeUIStateChanges()
        return binding.root
    }

    override fun onResume() {
        super.onResume()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
            checkAndShowNotGranted()
    }

    override fun observeUIStateChanges() =
        viewModel.notesState.observe(viewLifecycleOwner, adapter::submitList)

    @RequiresApi(Build.VERSION_CODES.S)
    private fun checkAndRequestAlarmPermission(): Boolean {
        val isGranted = alarmManager.canScheduleExactAlarms()
        if (!isGranted) startActivity(Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM))
        return isGranted
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun checkAndShowNotGranted() {
        if (!alarmManager.canScheduleExactAlarms())
            AlarmPermissionRequiredDialogFragment()
                .show(childFragmentManager, AlarmPermissionRequiredDialogFragment.TAG)
    }
}