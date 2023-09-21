package com.paranid5.daily_planner.presentation.fragments.notes_fragments.dated_notes

import android.Manifest
import android.app.AlarmManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import com.vmadalin.easypermissions.EasyPermissions
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DatedNotesFragment : Fragment(), UIStateChangesObserver, EasyPermissions.PermissionCallbacks {
    private companion object {
        private const val NOTIFICATIONS_PERMISSION_REQUEST_CODE = 20
    }

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
                requireContext(),
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
        binding = inflateViewBinding(inflater, container).apply { initView() }
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

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) = Unit

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) = Unit

    private fun inflateViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        DataBindingUtil.inflate<FragmentNotesBinding?>(
            inflater,
            R.layout.fragment_notes,
            container,
            false
        )

    private fun FragmentNotesBinding.initView() {
        addNoteFAB.setOnClickListener { onFABClicked() }

        notesList.also {
            it.layoutManager = LinearLayoutManager(context)
            it.adapter = adapter
            it.addItemDecoration(VerticalSpaceItemDecoration(30))
            touchHelper.attachToRecyclerView(it)
        }
    }

    private fun onFABClicked() {
        fun addNote() =
            viewModel.handler.onAddNoteButtonClicked(childFragmentManager)

        if (!checkAndRequestNotificationsPermission())
            return

        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.S ->
                if (checkAndRequestAlarmPermission()) addNote()
            else -> addNote()
        }
    }

    private fun checkAndRequestNotificationsPermission(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val isPostNotificationsGranted = EasyPermissions.hasPermissions(
                requireContext(), Manifest.permission.POST_NOTIFICATIONS
            )

            if (!isPostNotificationsGranted) {
                val isPostNotificationsDeclined = EasyPermissions.somePermissionPermanentlyDenied(
                    this, listOf(Manifest.permission.POST_NOTIFICATIONS)
                )

                if (isPostNotificationsDeclined)
                    Toast.makeText(
                        requireContext(),
                        R.string.notifications_required,
                        Toast.LENGTH_LONG
                    ).show()

                EasyPermissions.requestPermissions(
                    host = this@DatedNotesFragment,
                    rationale = resources.getString(R.string.notifications_required),
                    requestCode = NOTIFICATIONS_PERMISSION_REQUEST_CODE,
                    Manifest.permission.POST_NOTIFICATIONS
                )

                return false
            }
        }

        return true
    }

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