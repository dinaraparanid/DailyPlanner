package com.paranid5.daily_planner.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.paranid5.daily_planner.R
import com.paranid5.daily_planner.data.note.DatedNote
import com.paranid5.daily_planner.data.note.SimpleNote
import com.paranid5.daily_planner.data.room.notes.NotesRepository
import com.paranid5.daily_planner.di.DatedNotesState
import com.paranid5.daily_planner.di.SimpleNotesState
import com.paranid5.daily_planner.presentation.main_fragment.MainFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var fragmentFactory: GlobalFragmentFactory

    @Inject
    lateinit var notesRepository: NotesRepository

    @Inject
    @SimpleNotesState
    lateinit var simpleNotesState: MutableLiveData<List<SimpleNote>>

    @Inject
    @DatedNotesState
    lateinit var datedNotesState: MutableLiveData<List<DatedNote>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.fragmentFactory = fragmentFactory
        initFirstFragment()
        observerNotesUpdates()
    }

    private fun initFirstFragment() {
        if (supportFragmentManager.findFragmentById(R.id.fragment_container) == null)
            supportFragmentManager
                .beginTransaction()
                .add(
                    R.id.fragment_container,
                    MainFragment::class.java,
                    null
                )
                .commit()
    }

    private fun observerNotesUpdates() {
        observeSimpleNotesUpdates()
        observeDatedNotesUpdates()
    }

    private fun observeSimpleNotesUpdates() {
        lifecycleScope.launch(Dispatchers.IO) {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                notesRepository.simpleNotes.collectLatest(simpleNotesState::postValue)
            }
        }
    }

    private fun observeDatedNotesUpdates() {
        lifecycleScope.launch(Dispatchers.IO) {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                notesRepository.datedNotes.collectLatest(datedNotesState::postValue)
            }
        }
    }
}