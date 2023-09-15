package com.paranid5.daily_planner.presentation.notes_fragments

import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.paranid5.daily_planner.data.note.DatedNote
import com.paranid5.daily_planner.data.note.Note
import com.paranid5.daily_planner.data.note.SimpleNote
import com.paranid5.daily_planner.data.room.notes.NotesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class NotesTouchHandler(
    private val notesState: LiveData<List<Note>>,
    private val notesRepository: NotesRepository
) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT),
    CoroutineScope by MainScope() {
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ) = true // TODO: handle move order

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        when (val note = notesState.value!![viewHolder.adapterPosition]) {
            is DatedNote -> launch(Dispatchers.IO) { notesRepository.delete(note) }
            is SimpleNote -> launch(Dispatchers.IO) { notesRepository.delete(note) }
        }
    }
}