package com.ardritkrasniqi.moti.ui.todayFragment

import android.app.Application
import android.content.Context
import android.graphics.Color
import androidx.core.content.ContextCompat
import androidx.lifecycle.*
import com.ardritkrasniqi.moti.R
import com.ardritkrasniqi.moti.database.getDatabase
import com.ardritkrasniqi.moti.repository.WeatherRepository
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.lang.Exception
import java.util.*

class TodayViewModel(application: Application) : AndroidViewModel(application) {

    private val context = getApplication<Application>().applicationContext

    private val _status = MutableLiveData<String>()
    val status: LiveData<String>
        get() = _status


    // we get the weatherConditions from weatherConditions repository
    private val weatherRepository = WeatherRepository(getDatabase(context))
    val weather = weatherRepository.weatherList
    val cityNames = weatherRepository.cityList


    init {
        refreshWeatherFromRepository()
    }

    private fun refreshWeatherFromRepository() {
        viewModelScope.launch {
            try {
                weatherRepository.refreshWeather("Prizren", "metric")
            } catch (error: Exception) {
                _status.value = "Error: ${error.localizedMessage}"
            }
        }
    }


    fun getData(count: Int, range: Float, context: Context): LineData {
        val values =
            ArrayList<Entry>()
        for (i in 0 until count) {
            val `val` = (Math.random() * range).toFloat() + 3
            values.add(Entry(i.toFloat(), `val`))
        }

        // create a dataset and give it a type
        val set1 = LineDataSet(values, "Temperatura")
        set1.fillAlpha = 110;
        // set1.setFillColor(Color.RED);
        set1.lineWidth = 1.75f
        set1.circleRadius = 8f
        set1.circleHoleRadius = 4f
        set1.color = ContextCompat.getColor(context, R.color.violet)
        set1.setCircleColor(ContextCompat.getColor(context, R.color.violet))
        set1.highLightColor = Color.WHITE
        set1.setDrawValues(true)


        // create a data object with the data sets
        return LineData(set1)
    }

    fun setupChart(chart: LineChart, data: LineData, color: Int) {
        (data.getDataSetByIndex(0) as LineDataSet).circleHoleColor = color

        // no description text
        chart.description.isEnabled = false
        chart.setDrawMarkers(true)

        // chart.setDrawHorizontalGrid(false);
        //
        // enable / disable grid background
        chart.setDrawGridBackground(false)
        //        chart.getRenderer().getGridPaint().setGridColor(Color.WHITE & 0x70FFFFFF);

        // enable touch gestures
        chart.setTouchEnabled(true)

        // enable scaling and dragging
        chart.isDragEnabled = true
        chart.setScaleEnabled(false)

        // if disabled, scaling can be done on x- and y-axis separately
        chart.setPinchZoom(false)
        chart.setBackgroundColor(color)


        // set custom chart offsets (automatic offset calculation is hereby disabled)
        chart.setViewPortOffsets(30f, 0f, 30f, 0f)


        // add data
        chart.data = data

        // get the legend (only possible after setting data)
        val l = chart.legend
        l.isEnabled = false
        chart.axisLeft.isEnabled = false
        chart.axisLeft.spaceTop = 40f
        chart.axisLeft.spaceBottom = 40f
        chart.axisRight.isEnabled = false
        chart.xAxis.isEnabled = false

        // animate calls invalidate()...
        chart.animateX(400)
    }


    /**
     * Factory method to construct viewmodel with parameter( in this case the application parameter)
     */
    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(TodayViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return TodayViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}
