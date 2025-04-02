package com.example.project1729.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.project1729.R
import com.example.project1729.databinding.FragmentHistoryMainBinding


class HistoryMainFragment : Fragment() {

    private lateinit var binding: FragmentHistoryMainBinding


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentHistoryMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.historyMainButtonBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.historyMainFirstItem.setOnClickListener {
            findNavController().navigate(
                R.id.action_historyMainFragment_to_historyContentFragment,
                HistoryContentFragment.createArgs(HISTORY_CONTENT_RABKIN)
            )
        }

        binding.historyMainSecondItem.setOnClickListener {
            findNavController().navigate(
                R.id.action_historyMainFragment_to_historyContentFragment,
                HistoryContentFragment.createArgs(HISTORY_CONTENT_SIVTSEV)
            )
        }


//        binding.historyMainThirdItem.setOnClickListener {
//            findNavController().navigate(
//                R.id.action_historyMainFragment_to_historyContentFragment,
//                HistoryContentFragment.createArgs(HISTORY_CONTENT_KCHSM)
//            )
//        }
//
//        binding.historyMainFourthItem.setOnClickListener {
//            findNavController().navigate(
//                R.id.action_historyMainFragment_to_historyContentFragment,
//                HistoryContentFragment.createArgs(HISTORY_CONTENT_KCHSM)
//            )
//        }
    }


    companion object{
        const val HISTORY_CONTENT_RABKIN = "history_content_rabkin"
        const val HISTORY_CONTENT_SIVTSEV = "history_content_sivtsev"
        const val HISTORY_CONTENT_KCHSM = "history_content_KCHSM"
        const val HISTORY_CONTENT_PHOTO = "history_content_photo"
    }

}