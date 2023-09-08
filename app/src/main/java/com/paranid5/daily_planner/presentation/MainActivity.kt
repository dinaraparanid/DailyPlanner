package com.paranid5.daily_planner.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.paranid5.daily_planner.R
import com.paranid5.daily_planner.presentation.main_fragment.MainFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var fragmentFactory: GlobalFragmentFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.fragmentFactory = fragmentFactory
        initFirstFragment()
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
}