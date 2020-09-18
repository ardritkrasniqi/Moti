package com.ardritkrasniqi.moti.ui.selectAddedCities

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.ardritkrasniqi.moti.R
import com.ardritkrasniqi.moti.UtilityClasses.Constants
import com.ardritkrasniqi.moti.UtilityClasses.HelperClass
import com.ardritkrasniqi.moti.UtilityClasses.PrefUtils
import com.ardritkrasniqi.moti.adapters.CitiesAdapter
import com.ardritkrasniqi.moti.adapters.OnClickListener
import com.ardritkrasniqi.moti.databinding.CitiesFragmentBinding
import com.ardritkrasniqi.moti.ui.mainFragment.MainViewModel
import kotlinx.android.synthetic.main.forecast_fragment.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class Cities : Fragment() {

    companion object {
        fun newInstance() = Cities()
    }

    private lateinit var binding: CitiesFragmentBinding
    private lateinit var sharedPreff: PrefUtils
    private var selectedCityForDeletion = ""


    private val viewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
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
        binding.deleteIcon.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                if (selectedCityForDeletion.isNotEmpty()) {
                    viewModel.deleteCity(selectedCityForDeletion)
                    afterCityDeletion()
                    try {
                        viewModel.weatherList.value?.get(0)?.city?.name?.let { it1 ->
                            sharedPreff.save(Constants.SELECTED_CITY,
                                it1
                            )
                        }
                    } catch (e: Exception){
                        Log.e("exception", e.localizedMessage)
                    }

                }
            }
        }
        return binding.root
    }


    // sends the selected city back to todayFragment
    private fun initialiseBinding(inflater: LayoutInflater) {
        binding = CitiesFragmentBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.cityRecyclerView.adapter = CitiesAdapter(
            // listeneri i clickut
            OnClickListener({ cityName ->
                findNavController().previousBackStackEntry?.savedStateHandle?.set(
                    "key",
                    cityName
                )
                sharedPreff.save(Constants.SELECTED_CITY, cityName)
                findNavController().popBackStack()
            },
                // listeneri i longpressit
                {
                    selectedCityToDelete(it)
                })
        )

    }


    @SuppressLint("RestrictedApi")
    private fun afterCityDeletion() {
        binding.cityRecyclerView.adapter?.notifyDataSetChanged()
        binding.deleteIcon.visibility = View.GONE
        HelperClass.fabAnimationShow(binding.addCityFab)
        selectedCityForDeletion = ""
    }

    @SuppressLint("RestrictedApi")
    private fun selectedCityToDelete(it: String) {
        Log.d("clicked", "clicked $it my friend")
        selectedCityForDeletion = it
        if(binding.addCityFab.isVisible) {
            HelperClass.fabAnimationHide(binding.addCityFab)
        }
        binding.addCityFab.visibility = View.GONE
        binding.deleteIcon.visibility = View.VISIBLE
        binding.toolbar.navigationIcon = resources.getDrawable(R.drawable.ic_clear_24, null)
        binding.toolbar.title = ""


        binding.toolbar.setNavigationOnClickListener {
            afterCityDeletion()
            NavigationUI.setupWithNavController(binding.toolbar, findNavController())
            binding.toolbar.title = "Select city"
            binding.addCityFab.visibility = View.VISIBLE
        }

    }
}