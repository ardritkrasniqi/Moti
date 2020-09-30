package com.ardritkrasniqi.moti.ui.todayFragment

import android.content.Context
import android.content.Context.SENSOR_SERVICE
import android.hardware.Sensor
import android.hardware.Sensor.TYPE_ACCELEROMETER
import android.hardware.Sensor.TYPE_MAGNETIC_FIELD
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.hardware.SensorManager.SENSOR_DELAY_GAME
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation.RELATIVE_TO_SELF
import android.view.animation.RotateAnimation
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.ardritkrasniqi.moti.R
import com.ardritkrasniqi.moti.UtilityClasses.Constants
import com.ardritkrasniqi.moti.UtilityClasses.PrefUtils
import com.ardritkrasniqi.moti.databinding.TodayFragmentBinding
import com.ardritkrasniqi.moti.ui.mainFragment.MainViewModel
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.LineData
import java.lang.Math.toDegrees


/*
Brewed with love by Ardrit Krasniqi 2020
 */

class TodayFragment : Fragment(), SensorEventListener {

    companion object {
        fun newInstance() = TodayFragment()
    }

    lateinit var binding: TodayFragmentBinding
    private lateinit var sensorManager: SensorManager
    private lateinit var accelerometer: Sensor
    private lateinit var magnetometer: Sensor
    private lateinit var sharedPreff: PrefUtils
    private lateinit var geoCoder: Geocoder
    private lateinit var tempChart: LineChart

    var currentDegree = 0.0f
    var lastAccelerometer = FloatArray(3)
    var lastMagnetometer = FloatArray(3)
    var lastAccelerometerSet = false
    var lastMagnetometerSet = false


    // viewmodeli initializohet me lazy qe do te thote kur te krijohet aktiviteti i cili e hoston fragmentin
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
        sharedPreff = PrefUtils(requireContext(), Constants.SHAREDPREFF_NAME, Context.MODE_PRIVATE)
        binding = TodayFragmentBinding.inflate(inflater)
        // Allows Data Binding to Observe LiveData with the lifecycle of this Fragment
        // Lejon DataBinding qe te observoje liveDatan me (jeten) e ketij fragmneti
        binding.lifecycleOwner = this
        // Giving the binding access to the viewmodel
        // I jep akces bindit qe te perdore viewmodelin
        binding.viewModel = viewModel

        val lat = sharedPreff.getString(Constants.SELECTED_CITY_COORDINATES_LAT, "0.0")!!.toDouble()
        val lon = sharedPreff.getString(Constants.SELECTED_CITY_COORDINATES_LON, "0.0")!!.toDouble()

        // instantiating sensors
        sensorManager = (requireActivity().getSystemService(SENSOR_SERVICE) as SensorManager?)!!
        accelerometer = sensorManager.getDefaultSensor(TYPE_ACCELEROMETER)
        magnetometer = sensorManager.getDefaultSensor(TYPE_MAGNETIC_FIELD)
        // instantiates lineData from viewModel
        val lineData: LineData = viewModel.getData(5, 5f, requireContext())
        viewModel.setupChart(
            binding.tempChart,
            lineData,
            ContextCompat.getColor(requireContext(), R.color.whiteColor)
        )
        tempChart = binding.tempChart

         // we make the call for updated weather

        // Sets the icon in weatherDescriptionIcon
        viewModel.weather.observe(viewLifecycleOwner, Observer { weather ->
            requireNotNull(weather ?: viewModel.getWeatherFromDatabase(lat, lon))
            // changes the main icon
            binding.weatherConditionIcon.setImageResource(
                when (viewModel.weather.value?.weatherList?.get(0)?.weatherId) {
                    in 200..299 -> R.drawable.ic_thunderstorm_colored
                    in 300..399 -> R.drawable.ic_drizzle_colored_3xx
                    in 500..599 -> R.drawable.ic_lightrain_colored
                    in 600..699 -> R.drawable.ic_snow_colored
                    in 700..799 -> R.drawable.ic_cloud_mist_7xx
                    800 -> R.drawable.ic_sunny_colored
                    in 801..802 -> R.drawable.ic_fewclouds_colored_801
                    in 803..900 -> R.drawable.ic_moreclouds_colored
                    else -> R.drawable.ic_moon
                }
            )
            // changes the card icons
            binding.card1Image.setImageResource(cardWeatherConditionIconResource(viewModel.weather.value?.weatherList?.get(0)?.weatherId))
            binding.card2Image.setImageResource(cardWeatherConditionIconResource(viewModel.weather.value?.weatherList?.get(1)?.weatherId))
            binding.card3Image.setImageResource(cardWeatherConditionIconResource(viewModel.weather.value?.weatherList?.get(2)?.weatherId))

        })




            // goes to slectCityFragment
        binding.selectCity.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_cities)
        }
        // gets the cityName from selectCityFragment and uses in viewmodel for calls and room fetch
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<String>("key")
            ?.observe(
                viewLifecycleOwner
            ) { cityName ->
                viewModel.getWeatherName(cityName)
            }
        return binding.root
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        //nuk na duhet
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor === accelerometer) {
            lowPass(event.values, lastAccelerometer)
            lastAccelerometerSet = true
        } else if (event?.sensor === magnetometer) {
            lowPass(event.values, lastMagnetometer)
            lastMagnetometerSet = true
        }

        if (lastAccelerometerSet && lastMagnetometerSet) {
            val r = FloatArray(9)
            if (SensorManager.getRotationMatrix(r, null, lastAccelerometer, lastMagnetometer)) {
                val orientation = FloatArray(3)
                SensorManager.getOrientation(r, orientation)
                val degree = (toDegrees(orientation[0].toDouble()) + 360).toFloat() % 360

                val rotateAnimation = RotateAnimation(
                    currentDegree,
                    -degree,
                    RELATIVE_TO_SELF, 0.5f,
                    RELATIVE_TO_SELF, 0.5f
                )
                rotateAnimation.duration = 1000
                rotateAnimation.fillAfter = true

                binding.compassImage.startAnimation(rotateAnimation)
                currentDegree = -degree
            }
        }
    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(this, accelerometer, SENSOR_DELAY_GAME)
        sensorManager.registerListener(this, magnetometer, SENSOR_DELAY_GAME)
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this, accelerometer)
        sensorManager.unregisterListener(this, magnetometer)
    }

    private fun lowPass(input: FloatArray, output: FloatArray) {
        val alpha = 0.05f

        for (i in input.indices) {
            output[i] = output[i] + alpha * (input[i] - output[i])
        }
    }


    fun cardWeatherConditionIconResource(weatherCondition: Int?): Int {
        return when (weatherCondition) {
            in 200..299 -> R.drawable.ic_cloudy
            in 300..399 -> R.drawable.ic_cloudy
            in 500..599 -> R.drawable.ic_rain
            in 600..699 -> R.drawable.ic_snow_cloud_simple
            in 700..799 -> R.drawable.ic_cloudy
            800 -> R.drawable.ic_sunny
            in 801..802 -> R.drawable.ic_005_cloudy_day_1
            in 803..900 -> R.drawable.ic_cloudy
            else -> R.drawable.ic_moon
        }

    }

}
