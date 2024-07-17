package com.example.project1729.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableRow
import android.widget.TextView
import androidx.core.view.size
import androidx.fragment.app.Fragment
import com.example.project1729.R
import com.example.project1729.databinding.FragmentResultsTableBinding
import com.example.project1729.domain.model.Check
import com.example.project1729.ui.view_model.ResultsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.random.Random


class ResultsTableFragment : Fragment() {

    private lateinit var binding: FragmentResultsTableBinding
    private val viewModel by viewModel<ResultsViewModel>()
    private var currentChecksList = mutableListOf<Check>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentResultsTableBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getState().observe(viewLifecycleOwner){checksList -> render(checksList)

        }
    }


    private fun render(checksList: List<Check>){
        if (currentChecksList != checksList){
            currentChecksList = checksList.toMutableList()
        }
        binding.table.removeViews(1, binding.table.size - 1)
        for (i in currentChecksList.reversed()){
            binding.table.addView(createTableRow(i))
        }

        if (currentChecksList.isEmpty()){
            binding.table.visibility = View.GONE
            binding.resultsTablePlaceholder.visibility = View.VISIBLE
        }
        else{
            binding.table.visibility = View.VISIBLE
            binding.resultsTablePlaceholder.visibility = View.GONE
        }
    }

    private fun createTableRow(check: Check): TableRow{

        val row = LayoutInflater.from(requireActivity()).inflate(R.layout.table_row_layout, null) as TableRow
        (row.findViewById<View>(R.id.tableDate) as TextView).text = check.dateAndTime
        (row.findViewById<View>(R.id.tableKCHSM) as TextView).text = check.KCHSM
        (row.findViewById<View>(R.id.tableEye) as TextView).text = check.eye
        (row.findViewById<View>(R.id.tableMethod) as TextView).text = check.measurementMethod
        (row.findViewById<View>(R.id.tableSaturation) as TextView).text = check.diodeSaturation
        (row.findViewById<View>(R.id.tableColor) as TextView).text = check.diodeColor
        (row.findViewById<View>(R.id.tableSize) as TextView).text = check.diodeSize
        (row.findViewById<View>(R.id.tableBrightness) as TextView).text = check.roomBrightness
        (row.findViewById<View>(R.id.tablePressure) as TextView).text = check.roomPressure
        (row.findViewById<View>(R.id.tableTemperature) as TextView).text = check.roomTemperature
        (row.findViewById<View>(R.id.tableHumidity) as TextView).text = check.roomHumidity
        (row.findViewById<View>(R.id.tableLoading) as TextView).text = check.eyeLoading
        (row.findViewById<View>(R.id.tableLoadingDuration) as TextView).text = check.eyeLoadingDuration
        (row.findViewById<View>(R.id.tableTraining) as TextView).text = check.eyeTraining


        return row

    }

    override fun onResume() {
        super.onResume()
        viewModel.getChecks()
    }

}