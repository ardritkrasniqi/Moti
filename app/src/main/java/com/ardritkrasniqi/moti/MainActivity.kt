package com.ardritkrasniqi.moti

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.ardritkrasniqi.moti.databinding.ActivityMainBinding
import com.ardritkrasniqi.moti.ui.viewPager.ViewPagerAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewPagerAdapter = ViewPagerAdapter(supportFragmentManager, this.lifecycle)

        binding.apply {
            viewPager.adapter = viewPagerAdapter
            viewPager.registerOnPageChangeCallback(
                object : OnPageChangeCallback() {
                    override fun onPageSelected(position: Int) {
                        binding.bottomNav.menu.getItem(position).isChecked = true
                    }
                }
            )
            bottomNav.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
        }
    }
}
