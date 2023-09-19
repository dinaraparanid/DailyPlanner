package com.paranid5.daily_planner.presentation.dialogs.add_note_dialog

import android.view.View
import androidx.annotation.MainThread
import androidx.databinding.Bindable
import androidx.lifecycle.MutableLiveData
import com.paranid5.daily_planner.BR
import com.paranid5.daily_planner.data.note.Repetition
import com.paranid5.daily_planner.data.note.DatedNote
import com.paranid5.daily_planner.data.note.NoteType
import com.paranid5.daily_planner.data.note.SimpleNote
import com.paranid5.daily_planner.data.utils.ext.filledToTimeFormat
import com.paranid5.daily_planner.di.DatedNotesState
import com.paranid5.daily_planner.di.SimpleNotesState
import com.paranid5.daily_planner.presentation.ObservablePresenter
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.util.Calendar
import java.util.Date

data class AddNotePresenter @AssistedInject constructor(
    @SimpleNotesState val simpleNotesState: MutableLiveData<List<SimpleNote>>,
    @DatedNotesState val datedNotesState: MutableLiveData<List<DatedNote>>,
    @Assisted("note_type") val noteTypeState: MutableLiveData<NoteType>,
    @Assisted("title") val titleInputState: MutableLiveData<String>,
    @Assisted("description") val descriptionInputState: MutableLiveData<String>,
    @Assisted("date") val dateState: MutableLiveData<Long?>,
    @Assisted("time") val timeState: MutableLiveData<Pair<Int, Int>?>,
    @Assisted("repetition") val repetitionState: MutableLiveData<Repetition>
) : ObservablePresenter() {
    private val calendar by lazy {
        Calendar.getInstance()
    }

    inline var noteType
        get() = noteTypeState.value!!

        @MainThread
        set(value) {
            noteTypeState.value = value
            notifyPropertyChanged(BR.dateElementsVisibility)
        }

    @get:Bindable
    internal inline val shownDate
        @JvmName("getShownDate")
        get() = when (val date = dateState.value) {
            null -> ""
            else -> {
                calendar.time = Date(date)

                val day = calendar.get(Calendar.DAY_OF_MONTH).toString().filledToTimeFormat
                val month = (calendar.get(Calendar.MONTH) + 1).toString().filledToTimeFormat
                val year = calendar.get(Calendar.YEAR)

                "$day.$month.$year"
            }
        }

    fun notifyDateChanged() = notifyPropertyChanged(BR.shownDate)

    @get:Bindable
    inline val shownTime
        @JvmName("getShownTime")
        get() = timeState.value?.let { (h, m) ->
            "${h.toString().filledToTimeFormat}:${m.toString().filledToTimeFormat}"
        } ?: ""

    fun notifyTimeChanged() = notifyPropertyChanged(BR.shownTime)

    @get:Bindable
    val dateElementsVisibility
        @JvmName("getDateElementsVisibility")
        get() = if (noteType == NoteType.DATED) View.VISIBLE else View.GONE
}