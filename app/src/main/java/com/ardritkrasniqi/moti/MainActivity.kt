package com.ardritkrasniqi.moti

import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.ardritkrasniqi.moti.databinding.ActivityMainBinding
import com.ardritkrasniqi.moti.ui.mainFragment.MainFragment
import com.ardritkrasniqi.moti.ui.viewPager.ViewPagerAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewPagerAdapter: ViewPagerAdapter

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
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





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewPagerAdapter = ViewPagerAdapter(supportFragmentManager, this.lifecycle)

        binding.apply {
            viewPager.adapter = viewPagerAdapter
            viewPager.registerOnPageChangeCallback(
                object : OnPageChangeCallback(){
                    override fun onPageSelected(position: Int) {
                        binding.bottomNav.menu.getItem(position).isChecked = true
                    }
                }
            )
            bottomNav.setOnNavigationItemSelectedListener (onNavigationItemSelectedListener)
        }
    }
}
