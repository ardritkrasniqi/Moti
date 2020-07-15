package com.ardritkrasniqi.moti.domain

import java.util.*


data class WeatherForecastModel(
    val city: CityModel,
    val weatherList: List<WeatherModel>?
)


data class CityModel(
    val country: String? = "",
    val id: Long? = -1,
    val name: String? = "",
    val population: Int? = -1,
    val sunrise: Int? = -1,
    val sunset: Int? = -1
)

data class WeatherModel(
    val feelsLike: Int? = -1,
    val humidity: Int? = -1,
    val pressure: Int? = -1,
    val seaLevel: Int? = -1,
    val temp: Int? = -1,
    val tempMax: Int? = -1,
    val tempMin: Int? = -1,
    val dateUnix: Long? = -1,
    val cloudsPercentage: Int? = -1,
    val windSpeed: Int? = -1,
    val windDegree: Int? = -1,
    val dateText: String? = "",
    val description: String? = "",
    val icon: String? = "",
    val main: String? = ""
)


