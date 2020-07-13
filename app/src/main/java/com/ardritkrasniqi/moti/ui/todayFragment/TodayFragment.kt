package com.ardritkrasniqi.moti.ui.todayFragment

import android.hardware.SensorEventListener2
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ardritkrasniqi.moti.R
import com.ardritkrasniqi.moti.databinding.TodayFragmentBinding
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.LineData

/*
Brewed with love by Ardrit Krasniqi 2020
 */

class TodayFragment : Fragment(){

    companion object {
        fun newInstance() = TodayFragment()
    }

    private val viewModel: TodayViewModel by lazy {
        val activity = requireNotNull(this.activity) {}
        ViewModelProvider(
            this,
            TodayViewModel.Factory(activity.application)
        ).get(TodayViewModel::class.java)
    }

    private lateinit var tempChart: LineChart


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = TodayFragmentBinding.inflate(inflater)
        viewModel.weather.observe(viewLifecycleOwner, Observer {
            Log.i("prova", viewModel.weather.value.toString())
        })

        viewModel.cityNames.observe(viewLifecycleOwner, Observer {

        Log.i("provacity", viewModel.cityNames.value.toString())
        })


        // Allows Data Binding to Observe LiveData with the lifecycle of this Fragment
        // Lejon DataBinding qe te observoje liveDatan me (jeten) e ketij fragmneti
        binding.lifecycleOwner = this
        // Giving the binding access to the viewmodel
        // I jep akces bindit qe te perdore viewmodelin
        binding.viewModel = viewModel

        val lineData: LineData = viewModel.getData(5, 5f, requireContext())
        viewModel.setupChart(
            binding.tempChart,
            lineData,
            ContextCompat.getColor(requireContext(), R.color.whiteColor)
        )

        tempChart = binding.tempChart
        return binding.root
    }


}
