package com.paranid5.daily_planner.presentation.edit_note_dialog

import android.view.View
import androidx.lifecycle.MutableLiveData
import com.paranid5.daily_planner.data.note.DatedNote
import com.paranid5.daily_planner.data.note.Note
import com.paranid5.daily_planner.data.note.Repetition
import com.paranid5.daily_planner.data.note.SimpleNote
import com.paranid5.daily_planner.presentation.BasePresenter
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

data class EditNotePresenter @AssistedInject constructor(
    @Assisted("note") val initialNote: Note,
    @Assisted("title") val titleInputState: MutableLiveData<String>,
    @Assisted("description") val descriptionInputState: MutableLiveData<String>,
    @Assisted("repetition") val repetitionState: MutableLiveData<Repetition>
) : BasePresenter {
    @JvmField
    val dateElementsVisibility = when (initialNote) {
        is DatedNote -> View.VISIBLE
        is SimpleNote -> View.GONE
    }
}