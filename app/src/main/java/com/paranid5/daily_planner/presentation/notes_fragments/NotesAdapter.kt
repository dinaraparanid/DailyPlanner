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
import io.noties.markwon.Markwon
import io.noties.markwon.ext.latex.JLatexMathPlugin
import io.noties.markwon.ext.tables.TablePlugin
import io.noties.markwon.ext.tasklist.TaskListPlugin
import io.noties.markwon.ext.strikethrough.StrikethroughPlugin
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class NotesAdapter(
    context: Context,
    private val notesRepository: NotesRepository
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

    private val markwon by lazy {
        Markwon
            .builder(context)
            .usePlugins(
                listOf(
                    StrikethroughPlugin.create(),
                    JLatexMathPlugin.create(13F),
                    TablePlugin.create(context),
                    TaskListPlugin.create(context)
                )
            )
            .build()
    }

    inner class NotesHolder(private val noteBinding: ItemNoteBinding) :
        RecyclerView.ViewHolder(noteBinding.root) {
        fun bind(note: Note) {
            noteBinding.note = note

            markwon.setMarkdown(noteBinding.msg, note.message)

            noteBinding.isDoneChecker.setOnCheckedChangeListener { _, isChecked ->
                launch(Dispatchers.IO) { notesRepository.changeChecked(note, isChecked) }
            }

            noteBinding.executePendingBindings()
        }
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