package com.ardritkrasniqi.moti.ui.selectAddedCities

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.ardritkrasniqi.moti.R
import com.ardritkrasniqi.moti.UtilityClasses.Constants
import com.ardritkrasniqi.moti.UtilityClasses.PrefUtils
import com.ardritkrasniqi.moti.databinding.CitiesFragmentBinding
import com.ardritkrasniqi.moti.ui.todayFragment.TodayViewModel

class Cities : Fragment() {

    companion object {
        fun newInstance() = Cities()
    }

    private lateinit var binding: CitiesFragmentBinding
    private lateinit var sharedPreff: PrefUtils


    private val viewModel by lazy {
        ViewModelProvider(this).get(TodayViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initialiseBinding(inflater)
        sharedPreff = PrefUtils(requireContext(), Constants.SHAREDPREFF_NAME, Context.MODE_PRIVATE)
        NavigationUI.setupWithNavController(binding.toolbar, findNavController())
        binding.toolbar.title = "Select city"
        binding.addCityFab.setOnClickListener {
            findNavController().navigate(R.id.action_cities_to_addNewCityFragment)
        }

        return binding.root
    }




    // sends the selected city back to todayFragment
    private fun initialiseBinding(inflater: LayoutInflater) {
        binding = CitiesFragmentBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.cityRecyclerView.adapter = CitiesAdapter(OnClickListener {
            cityName -> findNavController().previousBackStackEntry?.savedStateHandle?.set("key", cityName)
            sharedPreff.save(Constants.SELECTED_CITY, cityName)
            findNavController().popBackStack()
        })
    }
}