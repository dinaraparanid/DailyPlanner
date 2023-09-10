package com.paranid5.daily_planner.presentation.main_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.paranid5.daily_planner.R
import com.paranid5.daily_planner.data.Note
import com.paranid5.daily_planner.databinding.FragmentMainBinding
import com.paranid5.daily_planner.databinding.ItemNoteBinding
import com.paranid5.daily_planner.presentation.UIStateChangesObserver
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment(), UIStateChangesObserver {
    private lateinit var binding: FragmentMainBinding
    private val viewModel by viewModels<MainFragmentViewModel>()
    private val adapter = NotesAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_main,
            container,
            false
        )

        observeUIStateChanges()
        return binding.root
    }

    override fun observeUIStateChanges() =
        viewModel.notesState.observe(viewLifecycleOwner, adapter::submitList)

    private class NotesAdapter : RecyclerView.Adapter<NotesAdapter.NotesHolder>() {
        private val differ = AsyncListDiffer(
            this,
            object : DiffUtil.ItemCallback<Note>() {
                override fun areItemsTheSame(oldItem: Note, newItem: Note) =
                    oldItem.id == newItem.id

                override fun areContentsTheSame(oldItem: Note, newItem: Note) =
                    oldItem == newItem
            }
        )

        private class NotesHolder(private val noteBinding: ItemNoteBinding) :
            RecyclerView.ViewHolder(noteBinding.root) {
            fun bind(note: Note) {
                noteBinding.note = note
                noteBinding.executePendingBindings()
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = NotesHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_note,
                parent,
                false
            )
        )

        override fun onBindViewHolder(holder: NotesHolder, position: Int) =
            holder.bind(differ.currentList[position])

        override fun getItemCount() = differ.currentList.size

        fun submitList(newNotes: List<Note>) = differ.submitList(newNotes)
    }
}