package com.example.project1729.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.project1729.R
import com.example.project1729.databinding.FragmentResultsBinding
import com.example.project1729.ui.adapters.ResultsViewPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator


class ResultsFragment : Fragment() {

    private lateinit var binding: FragmentResultsBinding
    private lateinit var tabMediator: TabLayoutMediator

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentResultsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewPager.adapter = ResultsViewPagerAdapter(childFragmentManager, lifecycle)

        tabMediator = TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when(position) {
                0 -> tab.setText(R.string.graph)
                1 -> tab.setText(R.string.table)
                2 -> tab.setText(R.string.list)
            }
        }
        tabMediator.attach()
    }
}