package com.paranid5.daily_planner.presentation.fragments.notes_fragments

import android.app.AlarmManager
import android.content.Context
import androidx.core.content.getSystemService
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.paranid5.daily_planner.data.note.DatedNote
import com.paranid5.daily_planner.data.note.Note
import com.paranid5.daily_planner.data.note.SimpleNote
import com.paranid5.daily_planner.data.room.notes.NotesRepository
import com.paranid5.daily_planner.domain.utils.ext.cancelNoteAlarm
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class NotesTouchHandler(
    private val context: Context,
    private val notesState: LiveData<List<Note>>,
    private val notesRepository: NotesRepository
) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT),
    CoroutineScope by MainScope() {
    private val alarmManager by lazy {
        context.getSystemService<AlarmManager>()!!
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ) = true // TODO: handle move order

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        when (val note = notesState.value!![viewHolder.adapterPosition]) {
            is DatedNote -> {
                alarmManager.cancelNoteAlarm(context, note)
                launch(Dispatchers.IO) { notesRepository.delete(note) }
            }

            is SimpleNote -> launch(Dispatchers.IO) { notesRepository.delete(note) }
        }
    }
}