package com.ardritkrasniqi.moti.ui.forecastFragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.ardritkrasniqi.moti.UtilityClasses.Constants
import com.ardritkrasniqi.moti.UtilityClasses.HelperClass
import com.ardritkrasniqi.moti.UtilityClasses.PrefUtils
import com.ardritkrasniqi.moti.adapters.ForecastAdapter
import com.ardritkrasniqi.moti.adapters.OnClickListenerForecast
import com.ardritkrasniqi.moti.databinding.ForecastFragmentBinding
import com.ardritkrasniqi.moti.ui.mainFragment.MainViewModel
import kotlinx.coroutines.runBlocking


class ForecastFragment : Fragment() {

    companion object {
        fun newInstance() = ForecastFragment()
    }

    private lateinit var binding: ForecastFragmentBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var sharedPreff: PrefUtils


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
        sharedPreff = PrefUtils(requireContext(), Constants.SHAREDPREFF_NAME, Context.MODE_PRIVATE)
        recyclerView = binding.recyclerView
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        sharedPreff.getString(Constants.SELECTED_CITY, "null")?.let { viewModel.getWeatherFromDatabase(it) } // gets the weather from db
        binding.recyclerView.adapter = ForecastAdapter(OnClickListenerForecast {
            Log.d("clicked", it.toString())
        })

        viewModel.weather.observe(viewLifecycleOwner, Observer {
            viewModel.addedFiveDaysForecastDates()
            viewModel.getFiveForecastDays()
            binding.dayOfWeek.text = viewModel.weather.value?.weatherList?.get(0)?.dateText?.let { it1 ->
                HelperClass.getDayForForecastRecyclerFull(
                    it1
                )
            }
        })

        viewModel.forecastWeatherModels.observe(viewLifecycleOwner, Observer {
            (binding.recyclerView.adapter as ForecastAdapter).notifyDataSetChanged()
        })


        return binding.root
    }
}
