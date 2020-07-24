package com.ardritkrasniqi.moti.ui.citiesFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.ardritkrasniqi.moti.databinding.CitiesFragmentBinding
import com.ardritkrasniqi.moti.ui.todayFragment.TodayViewModel

class Cities : Fragment() {

    companion object {
        fun newInstance() = Cities()
    }

    private lateinit var binding: CitiesFragmentBinding


    private val viewModel by lazy {
        ViewModelProvider(this).get(TodayViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initialiseBinding(inflater)


        return binding.root
    }


    private fun initialiseBinding(inflater: LayoutInflater){
        binding = CitiesFragmentBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.cityRecyclerView.adapter = CitiesAdapter()
    }

}
