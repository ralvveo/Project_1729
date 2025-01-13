package com.example.project1729.ui.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.project1729.ui.fragment.ResultsGraphFragment
import com.example.project1729.ui.fragment.ResultsListFragment
import com.example.project1729.ui.fragment.ResultsTableFragment

class ResultsViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle)
    : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> ResultsGraphFragment()
            1 -> ResultsTableFragment()
            else -> ResultsListFragment()
        }
    }
}