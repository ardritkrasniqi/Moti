package com.ardritkrasniqi.moti.ui.mainFragment

import android.app.Application
import android.content.Context
import android.graphics.Color
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.databinding.Bindable
import androidx.lifecycle.*
import com.ardritkrasniqi.moti.R
import com.ardritkrasniqi.moti.UtilityClasses.HelperClass
import com.ardritkrasniqi.moti.database.getDatabase
import com.ardritkrasniqi.moti.domain.WeatherForecastModel
import com.ardritkrasniqi.moti.domain.WeatherModel
import com.ardritkrasniqi.moti.repository.WeatherRepository
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.util.*

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val context = getApplication<Application>().applicationContext

    private val _status = MutableLiveData<String>()
    val status: LiveData<String>
        get() = _status

    var forecastDaysList = mutableListOf<String>()
    private var _forecastWeatherModels =
        MutableLiveData<MutableList<List<WeatherModel>>>()
    val forecastWeatherModels: MutableLiveData<MutableList<List<WeatherModel>>>
        get() = _forecastWeatherModels

    // variable for weather in forecastWeather, i use it to change the data according to wich date is selected
    private var _forecastModel = MutableLiveData<List<WeatherModel>>()
    val forecastModel: LiveData<List<WeatherModel>>
        get() = _forecastModel

    // we get the weather from WeatherRepository repository
    private val weatherRepository = WeatherRepository(getDatabase(context))

    // weather variables
    private var _weather: MutableLiveData<WeatherForecastModel> =
        weatherRepository.weatherList as MutableLiveData<WeatherForecastModel>
    val weather: LiveData<WeatherForecastModel>
        get() = _weather

    // list of all weathers variable
    val weatherList = weatherRepository.weatherListAllAsDomain


    // makes newwork call than takes weather from db
    fun getWeather(lat: Double, lon: Double): LiveData<WeatherForecastModel> {
        refreshWeatherFromRepository(lat, lon)
        _weather = weatherRepository.getCity(lat, lon) as MutableLiveData<WeatherForecastModel>
        return _weather
    }

    // makes newwork call than takes weather from db
    fun getWeatherName(city: String): LiveData<WeatherForecastModel> {
        refreshWeatherFromRepositoryName(city)
        _weather = weatherRepository.getCityName(city) as MutableLiveData<WeatherForecastModel>
        return _weather
    }


    // gets weather from db without making network call
    fun getWeatherFromDatabase(lat: Double, lon: Double){
        _weather = weatherRepository.getCity(lat, lon) as MutableLiveData<WeatherForecastModel>
    }



    // Funksioni i cili thirret nga initi i viewmodelit per te marre motin e updatuar
    // Ben thirrjen ne repository dhe i ben cache ne db
    private fun refreshWeatherFromRepository(lat: Double, lon: Double) {
        viewModelScope.launch {
            try {
                weatherRepository.refreshWeather(lat, lon, "metric")
            } catch (error: Exception) {
                _status.value = "Error: ${error.localizedMessage}"
            }
        }
    }

    private fun refreshWeatherFromRepositoryName(city: String) {
        viewModelScope.launch {
            try {
                weatherRepository.refreshWeatherName(city, "metric")
            } catch (error: Exception) {
                _status.value = "Error: ${error.localizedMessage}"
            }
        }
    }



    suspend fun deleteCity(city: String){
        withContext(Dispatchers.IO){
            weatherRepository.deleteCity(city)

        }
    }

    fun addedFiveDaysForecastDates() {
        if (_weather.value?.weatherList?.get(0) != null) {
            forecastDaysList = _weather.value?.weatherList?.get(0)?.dateText?.let {
                HelperClass.addFiveDaysForRecycler(it, 4)
            }!!
        }
    }


    fun getFiveForecastDays() {
        val mutableList = mutableListOf<List<WeatherModel>>()
            for (i in 0..4) {
                _weather.value?.weatherList?.filter { it.dateText!!.contains(forecastDaysList[i]) }
                    ?.let {
                        mutableList.add(
                            it
                        )
                    }
            }
            _forecastWeatherModels.value = mutableList
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
     * paterna "factory" per te konstruktu viewmodelin me parameter ne kete rast na duhet konteksti i aplikactionit per te initilizu DB
     */
    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}
