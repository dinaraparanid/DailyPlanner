package com.paranid5.daily_planner.presentation.notes_fragments.simple_notes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.paranid5.daily_planner.R
import com.paranid5.daily_planner.databinding.FragmentNotesBinding
import com.paranid5.daily_planner.presentation.UIStateChangesObserver
import com.paranid5.daily_planner.presentation.notes_fragments.NotesAdapter
import com.paranid5.daily_planner.presentation.utils.decorations.VerticalSpaceItemDecoration
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SimpleNotesFragment : Fragment(), UIStateChangesObserver {
    private lateinit var binding: FragmentNotesBinding
    private val viewModel by viewModels<SimpleNotesViewModel>()
    private val adapter = NotesAdapter()

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
            addNote.setOnClickListener {
                viewModel.handler.onAddNoteButtonClicked(childFragmentManager)
            }

            notesList.also {
                it.layoutManager = LinearLayoutManager(context)
                it.adapter = adapter
                it.addItemDecoration(VerticalSpaceItemDecoration(30))
            }
        }

        observeUIStateChanges()
        return binding.root
    }

    override fun observeUIStateChanges() =
        viewModel.notesState.observe(viewLifecycleOwner, adapter::submitList)
}