package com.ardritkrasniqi.moti.ui.mainFragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.ardritkrasniqi.moti.R
import com.ardritkrasniqi.moti.databinding.MainFragmentBinding
import com.ardritkrasniqi.moti.ui.viewPager.ViewPagerAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private val mainViewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }


    private lateinit var binding: MainFragmentBinding
    private lateinit var viewPagerAdapter: ViewPagerAdapter

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

            mainViewModel.weatherList.observe(viewLifecycleOwner, Observer {
                Log.i("viewmodeli", mainViewModel.weatherList.value?.get(0)?.weatherList?.get(0).toString())
            })

        }
        return binding.root
    }


}
