package com.ardritkrasniqi.moti.ui.todayFragment

import android.content.Context.SENSOR_SERVICE
import android.hardware.Sensor
import android.hardware.Sensor.TYPE_ACCELEROMETER
import android.hardware.Sensor.TYPE_MAGNETIC_FIELD
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.hardware.SensorManager.SENSOR_DELAY_GAME
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation.RELATIVE_TO_SELF
import android.view.animation.RotateAnimation
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.ardritkrasniqi.moti.R
import com.ardritkrasniqi.moti.databinding.TodayFragmentBinding
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.LineData
import java.lang.Math.toDegrees

/*
Brewed with love by Ardrit Krasniqi 2020
 */

class TodayFragment : Fragment(), SensorEventListener {

    lateinit var binding: TodayFragmentBinding
    private lateinit var sensorManager: SensorManager
    private lateinit var accelerometer: Sensor
    private lateinit var magnetometer: Sensor

    var currentDegree = 0.0f
    var lastAccelerometer = FloatArray(3)
    var lastMagnetometer = FloatArray(3)
    var lastAccelerometerSet = false
    var lastMagnetometerSet = false

    companion object {
        fun newInstance() = TodayFragment()
    }


    // viewmodeli initializohet me lazy qe do te thote kur te krijohet aktiviteti i cili e hoston fragmentin
    private val viewModel: TodayViewModel by lazy {
        val activity = requireNotNull(this.activity) {}
        ViewModelProvider(
            this,
            TodayViewModel.Factory(activity.application)
        ).get(TodayViewModel::class.java)
    }

    private lateinit var tempChart: LineChart


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = TodayFragmentBinding.inflate(inflater)
        // Allows Data Binding to Observe LiveData with the lifecycle of this Fragment
        // Lejon DataBinding qe te observoje liveDatan me (jeten) e ketij fragmneti
        binding.lifecycleOwner = this
        // Giving the binding access to the viewmodel
        // I jep akces bindit qe te perdore viewmodelin
        binding.viewModel = viewModel

        // instantiating sensors
        sensorManager = (requireActivity().getSystemService(SENSOR_SERVICE) as SensorManager?)!!
        accelerometer = sensorManager.getDefaultSensor(TYPE_ACCELEROMETER)
        magnetometer = sensorManager.getDefaultSensor(TYPE_MAGNETIC_FIELD)


        val lineData: LineData = viewModel.getData(5, 5f, requireContext())
        viewModel.setupChart(
            binding.tempChart,
            lineData,
            ContextCompat.getColor(requireContext(), R.color.whiteColor)
        )

        tempChart = binding.tempChart
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

}
