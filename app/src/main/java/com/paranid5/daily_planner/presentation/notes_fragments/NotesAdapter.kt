package com.paranid5.daily_planner.presentation.notes_fragments

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.paranid5.daily_planner.data.note.Note
import com.paranid5.daily_planner.data.room.notes.NotesRepository
import com.paranid5.daily_planner.databinding.ItemNoteBinding
import com.paranid5.daily_planner.presentation.utils.ext.DefaultMarkwon
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class NotesAdapter(
    context: Context,
    private val notesRepository: NotesRepository,
    private val onItemClicked: (Note) -> Unit = {}
) : RecyclerView.Adapter<NotesAdapter.NotesHolder>(),
    CoroutineScope by MainScope() {
    private val differ by lazy {
        AsyncListDiffer(this, object : DiffUtil.ItemCallback<Note>() {
            override fun areItemsTheSame(oldItem: Note, newItem: Note) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Note, newItem: Note) =
                oldItem == newItem
        })
    }

    internal val mMarkwon by lazy { DefaultMarkwon(context) }

    inner class NotesHolder(private val noteBinding: ItemNoteBinding) :
        RecyclerView.ViewHolder(noteBinding.root) {
        fun bind(note: Note) {
            noteBinding.note = note

            mMarkwon.setMarkdown(noteBinding.msg, note.message)
            noteBinding.msg.setOnClickListener { onClicked() }

            noteBinding.isDoneChecker.setOnCheckedChangeListener { _, isChecked ->
                launch(Dispatchers.IO) { notesRepository.changeChecked(note, isChecked) }
            }

            noteBinding.root.setOnClickListener { onClicked() }
            noteBinding.executePendingBindings()
        }

        private fun onClicked() = onItemClicked(noteBinding.note!!)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = NotesHolder(
        ItemNoteBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: NotesHolder, position: Int) =
        holder.bind(differ.currentList[position])

    override fun getItemCount() = differ.currentList.size

    fun submitList(newNotes: List<Note>) = differ.submitList(newNotes)
}