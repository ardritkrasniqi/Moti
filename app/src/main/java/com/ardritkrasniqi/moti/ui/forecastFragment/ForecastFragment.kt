package com.ardritkrasniqi.moti.ui.forecastFragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView

import com.ardritkrasniqi.moti.databinding.ForecastFragmentBinding
import com.ardritkrasniqi.moti.domain.WeatherModel
import com.ardritkrasniqi.moti.ui.mainFragment.MainViewModel


class ForecastFragment : Fragment() {

    companion object {
        fun newInstance() = ForecastFragment()
    }

    private lateinit var binding: ForecastFragmentBinding
    private lateinit var recyclerView: RecyclerView

    private val viewModel: MainViewModel by lazy {
        val activity = requireNotNull(this.activity) {}
        ViewModelProvider(
            this,
            MainViewModel.Factory(activity.application)
        ).get(MainViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ForecastFragmentBinding.inflate(inflater)
        recyclerView = binding.recyclerView
        recyclerView.adapter = ForecastAdapter(viewModel.forecastWeatherModels.value!!)


        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        viewModel.weather.observe(viewLifecycleOwner, Observer {
            viewModel.addedFiveDaysForecastDates()
            viewModel.getFiveForecastDays()
            (recyclerView.adapter as ForecastAdapter).notifyDataSetChanged()
        })


        viewModel.forecastWeatherModels.observe(viewLifecycleOwner, Observer {
            (recyclerView.adapter as ForecastAdapter).notifyDataSetChanged()
        })

        return binding.root
    }
}
