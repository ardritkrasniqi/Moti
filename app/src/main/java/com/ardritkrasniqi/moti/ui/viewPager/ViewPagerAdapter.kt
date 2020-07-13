package com.ardritkrasniqi.moti.ui.viewPager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ardritkrasniqi.moti.ui.forecastFragment.ForecastFragment
import com.ardritkrasniqi.moti.ui.settingsFragment.SettingsFragment
import com.ardritkrasniqi.moti.ui.todayFragment.TodayFragment

class ViewPagerAdapter(fragment: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragment, lifecycle) {

    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> TodayFragment()
            1 -> ForecastFragment()
            else -> SettingsFragment()
        }
    }

}