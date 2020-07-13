package com.ardritkrasniqi.moti.database

import androidx.room.*
import com.ardritkrasniqi.moti.domain.CityModel
import com.ardritkrasniqi.moti.domain.WeatherForecastModel
import com.ardritkrasniqi.moti.domain.WeatherModel
import kotlin.math.roundToInt


@Entity
data class WeatherEntity constructor(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Long,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "country")
    val country: String,
    @ColumnInfo(name = "population")
    val population: Int,
    @ColumnInfo(name = "sunrise")
    val sunrise: Int,
    @ColumnInfo(name = "sunset")
    val sunset: Int,
    @ColumnInfo(name = "list")
    val weather: List<Weather>
)


data class Weather(
    var temp: Double? = 0.0,
    @ColumnInfo(name = "dt") val dateUnix: Long? = -1,
    @ColumnInfo(name = "feels_like") val feelsLike: Double? = 0.0,
    @ColumnInfo(name = "temp_min") val tempMin: Double? = 0.0,
    @ColumnInfo(name = "temp_max") val tempMax: Double? = 0.0,
    val pressure: Int? = -1,
    @ColumnInfo(name = "sea_level") val seaLevel: Int? = -1,
    val humidity: Int? = -1,
    @ColumnInfo(name = "id") val weatherId: Int? = -1,
    val main: String? = "",
    @ColumnInfo(name = "description") val weatherDescription: String? = "",
    val icon: String? = "",
    @ColumnInfo(name = "all") val cloudsPercentage: Int? = -1,
    val speed: Double? = 0.0,
    @ColumnInfo(name = "deg")
    val windDeg: Int? = -1,
    @ColumnInfo(name = "dt_txt") val dateText: String? = ""
)


fun WeatherEntity.asDomainModel(): WeatherForecastModel {
    return WeatherForecastModel(
        CityModel(
            country = this.country,
            name = this.name,
            population = this.population,
            sunrise = this.sunrise,
            sunset = this.sunset,
            id = this.id
        ),
        weatherList = weather.map {
            WeatherModel(
                feelsLike = it.feelsLike?.roundToInt(),
                humidity = it.humidity,
                pressure = it.pressure,
                seaLevel = it.seaLevel,
                tempMax = it.tempMax?.roundToInt(),
                temp = it.temp?.roundToInt(),
                tempMin = it.tempMin?.roundToInt(),
                dateUnix = it.dateUnix,
                cloudsPercentage = it.cloudsPercentage,
                windDegree = it.windDeg,
                windSpeed = it.speed?.roundToInt(),
                dateText = it.dateText,
                description = it.weatherDescription,
                icon = it.icon,
                main = it.main
            )
        }
    )
}
