package com.paranid5.daily_planner.presentation.main_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.paranid5.daily_planner.R
import com.paranid5.daily_planner.databinding.FragmentMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate<FragmentMainBinding?>(
            inflater,
            R.layout.fragment_main,
            container,
            false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.notesPager.adapter = MainPagerAdapter(requireActivity())

        TabLayoutMediator(binding.tabLayout, binding.notesPager) { tab, position ->
            tab.setText(
                when (position) {
                    0 -> R.string.simple
                    1 -> R.string.dated
                    else -> throw IllegalArgumentException("Illegal position in pager's tab")
                }
            )
        }.attach()
    }
}