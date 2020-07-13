package com.ardritkrasniqi.moti.repository

import android.util.Log
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


    suspend fun refreshWeather(city: String, unit: String){
        withContext(Dispatchers.IO){
            val weatherList = MotiNetwork.motiService.getForecastWeather(city, unit).await()
            database.WeatherDao.insertWeather(weatherList.asDatabaseModel())
        }
    }

    val weatherList: LiveData<WeatherForecastModel> = Transformations.map(database.WeatherDao.getWeatherByCityId("Prizren")){
        it.asDomainModel()
    }
    val cityList: LiveData<List<String>> = database.WeatherDao.getCityNames()

}