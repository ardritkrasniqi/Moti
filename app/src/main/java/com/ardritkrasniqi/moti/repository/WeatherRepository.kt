package com.ardritkrasniqi.moti.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.ardritkrasniqi.moti.database.WeatherDatabase
import com.ardritkrasniqi.moti.database.asDomainModel
import com.ardritkrasniqi.moti.domain.WeatherForecastModel
import com.ardritkrasniqi.moti.network.MotiNetwork
import com.ardritkrasniqi.moti.network.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WeatherRepository(private val database: WeatherDatabase) {

    /**
     * Ardrit Krasniqi 2020
     * Refresh the weatherConditions stored in the offline cache.
     *
     * This function uses the IO dispatcher to ensure the database insert database operation
     * happens on the IO dispatcher. By switching to the IO dispatcher using `withContext` this
     * function is now safe to call from any thread including the Main thread.
     *
     */

    suspend fun refreshWeather(city: String, unit: String) {
        withContext(Dispatchers.IO) {
            val weatherList = MotiNetwork.motiService.getForecastWeather(city, unit).await()
            database.WeatherDao.insertWeather(weatherList.asDatabaseModel())
        }
    }

    /**
     *  Marrim qytetin nga DB me func getWeatherByCityId nga Room , dhe e transformojme nga WeatherEntity ne DomainModel(Model i cili perdoret branda appit)
     *  Per me shume rreth DomainModel referohu tek domainModeli
     */
    var weatherList: LiveData<WeatherForecastModel> =
        Transformations.map(database.WeatherDao.getWeather()) {
            it?.asDomainModel()
        }

    fun getCity(city: String): LiveData<WeatherForecastModel>{
        weatherList = Transformations.map(database.WeatherDao.getWeatherByCityId(city)){
            it?.asDomainModel()
        }
        return weatherList
    }

    val cityList: LiveData<List<String>> = database.WeatherDao.getCityNames()
    // I transform a liveData as List to another LiveData<List>
    private val weatherListAllAsDatabase = database.WeatherDao.getWeatherList()
    val weatherListAllAsDomain: LiveData<List<WeatherForecastModel>> =
        Transformations.map(weatherListAllAsDatabase)
        {it -> it.map { it.asDomainModel() }}

}