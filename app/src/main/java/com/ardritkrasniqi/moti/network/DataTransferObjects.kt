package com.ardritkrasniqi.moti.network


import com.ardritkrasniqi.moti.database.WeatherEntity
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class WeatherList(
    @Json(name = "city")
    val city: City,
    @Json(name = "list")
    val weatherAllList: List<WeatherAll>
)

@JsonClass(generateAdapter = true)
data class City(
    val country: String?,
    @Json(name = "id")
    val id: Long?,
    val name: String?,
    val population: Int?,
    val sunrise: Int?,
    val sunset: Int?,
    val coord: Coord,
    val timezone: Int?
)

@JsonClass(generateAdapter = true)
@Json(name = "coord")
data class Coord(
    @Json(name = "lat")
    val lat: Double,
    @Json(name = "lon")
    val lon: Double
)

@JsonClass(generateAdapter = true)
@Json(name = "main")
data class Main(
    @Json(name = "temp")
    val temp: Double,
    @Json(name = "feels_like")
    val feelsLike: Double?,
    @Json(name = "temp_max")
    val tempMax: Double?,
    @Json(name = "temp_min")
    val tempMin: Double?,
    @Json(name = "pressure")
    val pressure: Int?,
    @Json(name = "sea_level")
    val seaLevel: Int?,
    @Json(name = "grnd_level")
    val grndLevel: Int?,
    @Json(name = "humidity")
    val humidity: Int?,
    @Json(name = "temp_kf")
    val tempKf: Double?
)
@JsonClass(generateAdapter = true)
@Json(name = "weather")
data class Weather(
    @Json(name = "id")
    val weatherId: Int?,
    @Json(name = "main")
    val mainDescription: String,
    @Json(name = "description")
    val description: String?,
    @Json(name = "icon")
    val icon: String?
)
@JsonClass(generateAdapter = true)
@Json(name = "clouds")
data class Clouds(
    @Json(name = "all")
    val cloudsPercentage: Int?
)
@JsonClass(generateAdapter = true)
@Json(name = "wind")
data class Wind(
    @Json(name = "speed")
    val windSpeed: Double?,
    @Json(name = "deg")
    val windDegree: Int?
)
@JsonClass(generateAdapter = true)
@Json(name = "rain")
data class Rain(
    @Json(name = "3h")
    val rainLastThreeHours: Double
)



@JsonClass(generateAdapter = true)
data class WeatherAll(
    @Json(name = "dt")
    val dateUnix: Long?,
    @Json(name = "main")
    val main: Main?,
    @Json(name = "weather")
    val weather: List<Weather>,
    @Json(name = "clouds")
    val clouds: Clouds,
    @Json(name = "wind")
    val wind: Wind,
    val rain: Rain?,
    @Json(name = "dt_txt")
    val dateText: String?
)


/**
 *Convert network results to database entities
 * Konvertimi i resulteve qe vijne nga networku ne entitet te databazes
 */
fun WeatherList.asDatabaseModel(): WeatherEntity {
    return WeatherEntity(
        id = this.city.id,
        name = this.city.name,
        country = this.city.country,
        population = this.city.population,
        sunset = this.city.sunset,
        sunrise = this.city.sunrise,
        lat = this.city.coord.lat,
        lon = this.city.coord.lon,
        weather = weatherAllList.map {
            com.ardritkrasniqi.moti.database.Weather(
                dateUnix = it.dateUnix,
                temp = it.main?.temp,
                feelsLike = it.main?.feelsLike,
                tempMin = it.main?.tempMin,
                tempMax = it.main?.tempMax,
                pressure = it.main?.pressure,
                seaLevel = it.main?.seaLevel,
                humidity = it.main?.humidity,
                weatherId = it.weather[0].weatherId,
                main = it.weather[0].mainDescription,
                weatherDescription = it.weather[0].description,
                icon = it.weather[0].icon,
                cloudsPercentage = it.clouds.cloudsPercentage,
                speed = it.wind.windSpeed,
                windDeg = it.wind.windDegree,
                dateText = it.dateText
            )
        }
    )
}

