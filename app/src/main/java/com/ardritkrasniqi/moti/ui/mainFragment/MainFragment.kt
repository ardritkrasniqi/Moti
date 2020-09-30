package com.ardritkrasniqi.moti.ui.mainFragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.ardritkrasniqi.moti.R
import com.ardritkrasniqi.moti.UtilityClasses.Constants
import com.ardritkrasniqi.moti.UtilityClasses.PrefUtils
import com.ardritkrasniqi.moti.databinding.MainFragmentBinding
import com.ardritkrasniqi.moti.ui.viewPager.ViewPagerAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }




    private lateinit var sharedPreff: PrefUtils
    private lateinit var binding: MainFragmentBinding
    private lateinit var viewPagerAdapter: ViewPagerAdapter
    private var lat: Double = 0.0
    private var lon: Double = 0.0

    private val onNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.todayFragment -> {
                    binding.viewPager.currentItem = 0
                    return@OnNavigationItemSelectedListener true
                }
                R.id.forecastFragment -> {
                    binding.viewPager.currentItem = 1
                    return@OnNavigationItemSelectedListener true
                }
                R.id.settingsFragment -> {
                    binding.viewPager.currentItem = 2
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = MainFragmentBinding.inflate(inflater)
        viewPagerAdapter = ViewPagerAdapter(childFragmentManager, this.lifecycle)
        sharedPreff = PrefUtils(requireContext(), Constants.SHAREDPREFF_NAME, Context.MODE_PRIVATE)
        lat = sharedPreff.getString(Constants.SELECTED_CITY_COORDINATES_LAT, "0.0")!!.toDouble()
        lon = sharedPreff.getString(Constants.SELECTED_CITY_COORDINATES_LON, "0.0")!!.toDouble()
        binding.apply {
            viewPager.adapter = viewPagerAdapter
            viewPager.registerOnPageChangeCallback(
                object : ViewPager2.OnPageChangeCallback() {
                    override fun onPageSelected(position: Int) {
                        binding.bottomNav.menu.getItem(position).isChecked = true
                    }
                }
            )
            bottomNav.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
        }

        viewModel.getWeather(lat, lon)
        return binding.root
    }


}
