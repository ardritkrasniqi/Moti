package com.ardritkrasniqi.moti.domain

import java.util.*


data class WeatherForecastModel(
    val city: CityModel,
    val weatherList: List<WeatherModel>?
)


data class CityModel(
    val country: String?,
    val id: Long?,
    val name: String?,
    val population: Int?,
    val sunrise: Int?,
    val sunset: Int?
)

data class WeatherModel(
    val feelsLike: Int?,
    val humidity: Int?,
    val pressure: Int?,
    val seaLevel: Int?,
    val temp: Int?,
    val tempMax: Int?,
    val tempMin: Int?,
    val dateUnix: Long?,
    val cloudsPercentage: Int?,
    val windSpeed: Int?,
    val windDegree: Int?,
    val dateText: String?,
    val description: String?,
    val icon: String?,
    val main: String?
)


