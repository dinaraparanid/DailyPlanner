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
import com.paranid5.daily_planner.domain.GitHubApi
import com.paranid5.daily_planner.domain.checkForUpdatesAsync
import com.paranid5.daily_planner.presentation.dialogs.NewReleaseDialogFragment
import com.paranid5.daily_planner.presentation.fragments.main_fragment.MainFragment
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

    @Inject
    lateinit var gitHubApi: GitHubApi

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Base_Theme_DailyPlanner)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.fragmentFactory = fragmentFactory
        initFirstFragment()
        observeNotesUpdates()
        lifecycleScope.launch { checkForUpdates() }
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

    private fun observeNotesUpdates() {
        observeSimpleNotesUpdates()
        observeDatedNotesUpdates()
    }

    private fun observeSimpleNotesUpdates() = lifecycleScope.launch(Dispatchers.IO) {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            notesRepository.simpleNotes.collectLatest(simpleNotesState::postValue)
        }
    }

    private fun observeDatedNotesUpdates() = lifecycleScope.launch(Dispatchers.IO) {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            notesRepository.datedNotes.collectLatest(datedNotesState::postValue)
        }
    }

    private fun checkForUpdates() = lifecycleScope.launch(Dispatchers.Main) {
        gitHubApi.checkForUpdatesAsync()?.let {
            NewReleaseDialogFragment
                .newInstance(it)
                .show(supportFragmentManager, NewReleaseDialogFragment.TAG)
        }
    }
}