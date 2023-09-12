package com.paranid5.daily_planner.presentation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.paranid5.daily_planner.presentation.main_fragment.MainFragment
import com.paranid5.daily_planner.presentation.notes_fragments.dated_notes.DatedNotesFragment
import com.paranid5.daily_planner.presentation.notes_fragments.simple_notes.SimpleNotesFragment
import javax.inject.Inject

class GlobalFragmentFactory @Inject constructor() : FragmentFactory() {
    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        if (className == MainFragment::class.java.name)
            return MainFragment()

        if (className == SimpleNotesFragment::class.java.name)
            return SimpleNotesFragment()

        if (className == DatedNotesFragment::class.java.name)
            return DatedNotesFragment()

        return super.instantiate(classLoader, className)
    }
}