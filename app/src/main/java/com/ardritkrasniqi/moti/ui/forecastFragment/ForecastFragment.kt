package com.ardritkrasniqi.moti.ui.forecastFragment

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider

import com.ardritkrasniqi.moti.R
import com.ardritkrasniqi.moti.databinding.ForecastFragmentBinding
import com.ardritkrasniqi.moti.ui.mainFragment.MainViewModel


class ForecastFragment : Fragment() {

    companion object {
        fun newInstance() = ForecastFragment()
    }

    private lateinit var binding: ForecastFragmentBinding

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

        binding.lifecycleOwner = this
        binding.viewModel = viewModel


        return binding.root
    }


}
