package com.paranid5.daily_planner.presentation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.paranid5.daily_planner.presentation.main_fragment.MainFragment
import javax.inject.Inject

class GlobalFragmentFactory @Inject constructor() : FragmentFactory() {
    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        if (className == MainFragment::class.java.name)
            return MainFragment()

        return super.instantiate(classLoader, className)
    }
}