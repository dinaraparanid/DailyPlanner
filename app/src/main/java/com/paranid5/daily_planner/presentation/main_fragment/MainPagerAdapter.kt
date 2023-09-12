package com.paranid5.daily_planner.presentation.main_fragment

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.paranid5.daily_planner.presentation.notes_fragments.dated_notes.DatedNotesFragment
import com.paranid5.daily_planner.presentation.notes_fragments.simple_notes.SimpleNotesFragment

class MainPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
    override fun createFragment(position: Int): Fragment = when (position) {
        0 -> SimpleNotesFragment()
        1 -> DatedNotesFragment()
        else -> throw IllegalArgumentException("Illegal fragment position")
    }

    override fun getItemCount() = 2
}