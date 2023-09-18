package com.paranid5.daily_planner.presentation.dialogs.note_details_dialog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.paranid5.daily_planner.data.utils.ext.repetitionStrRes
import com.paranid5.daily_planner.data.note.Note
import com.paranid5.daily_planner.di.NoteDetailsPresenterFactory
import com.paranid5.daily_planner.di.NoteDetailsViewModelFactory
import com.paranid5.daily_planner.presentation.ObservableViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

class NoteDetailsViewModel @AssistedInject constructor(
    presenterFactory: NoteDetailsPresenterFactory,
    override val handler: NoteDetailsUIHandler,
    @Assisted note: Note,
) : ObservableViewModel<NoteDetailsPresenter, NoteDetailsUIHandler>() {
    companion object {
        fun provideFactory(
            assistedFactory: NoteDetailsViewModelFactory,
            note: Note
        ) = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>) =
                assistedFactory.create(note) as T
        }
    }

    override val presenter = presenterFactory.create(note)

    inline val note
        get() = presenter.note

    inline val dateElementsVisibility
        get() = presenter.dateElementsVisibility

    inline val dateMessage   // for dated notes it is always its date,
        get() = note.message // otherwise it isn't shown

    inline val repetitionMessage
        get() = note.repetitionStrRes
}