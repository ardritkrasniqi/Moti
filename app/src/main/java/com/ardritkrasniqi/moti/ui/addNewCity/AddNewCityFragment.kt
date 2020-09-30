package com.ardritkrasniqi.moti.ui.addNewCity

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.RecyclerView
import com.ardritkrasniqi.moti.R
import com.ardritkrasniqi.moti.UtilityClasses.Constants
import com.ardritkrasniqi.moti.UtilityClasses.HelperClass
import com.ardritkrasniqi.moti.UtilityClasses.PrefUtils
import com.ardritkrasniqi.moti.databinding.AddNewCityFragmentBinding
import com.ardritkrasniqi.moti.network.CityData

class AddNewCityFragment : Fragment() {

    companion object {
        fun newInstance() = AddNewCityFragment()
    }


    private lateinit var binding: AddNewCityFragmentBinding
    private val viewModel by lazy {
        val activity = requireNotNull(this.activity) {}
        ViewModelProvider(this, AddNewCityViewModel.Factory(activity.application)).get(
            AddNewCityViewModel::class.java
        )
    }

    private var cityList: MutableList<CityData> = mutableListOf()
    private lateinit var recyclerView: RecyclerView
    private lateinit var sharedPreff: PrefUtils


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initializeBinding(inflater)
        binding.editText.setupClearButtonWithAction()
        NavigationUI.setupWithNavController(binding.toolbar, findNavController())
        recyclerView = binding.cityResultsFragment
        sharedPreff =
            PrefUtils.with(requireContext(), Constants.SHAREDPREFF_NAME, Context.MODE_PRIVATE)


        binding.editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // nothing
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (count > 2) {
                    viewModel.getCityByPrefix(s.toString())
                    Log.d("called", "im called from textchanged")
                }
            }
        })

        viewModel.cityByPrefixLiveData.observe(viewLifecycleOwner, Observer { data ->
            cityList = mutableListOf()   // clearing the list
            cityList.addAll(data.cityList)   // populating the list with new citys
            recyclerView.adapter = CityAdapter(cityList) { cityName ->
                HelperClass.hideSoftKeyBoard(
                    requireContext(),
                    binding.editText
                )   // hides keyboard before navigating
                // saves the selected city in sharedPreff
                sharedPreff.save(
                    Constants.SELECTED_CITY,
                    cityName.city
                )
                sharedPreff.save(
                    Constants.SELECTED_CITY_COORDINATES_LAT,
                    cityName.latitude
                )
                sharedPreff.save(
                    Constants.SELECTED_CITY_COORDINATES_LON,
                    cityName.longitude
                )
                findNavController().navigate(R.id.action_addNewCityFragment_to_mainFragment)   // pewwwwww wer navigating
            }
        })

        return binding.root
    }


    private fun initializeBinding(inflater: LayoutInflater) {
        binding = AddNewCityFragmentBinding.inflate(inflater)
    }

    @SuppressLint("ClickableViewAccessibility")
    fun EditText.setupClearButtonWithAction() {

        addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(editable: Editable?) {
                val clearIcon = if (editable?.isNotEmpty() == true) R.drawable.ic_clear_24 else 0
                setCompoundDrawablesWithIntrinsicBounds(0, 0, clearIcon, 0)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) =
                Unit

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (start > 2) {
                    viewModel.getCityByPrefix(s.toString())
                    Log.d("called", "im called from textchanged")
                }
            }
        })

        setOnTouchListener(View.OnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= (this.right - this.compoundPaddingRight)) {
                    this.setText("")
                    return@OnTouchListener true
                }
            }
            return@OnTouchListener false
        })
    }

}