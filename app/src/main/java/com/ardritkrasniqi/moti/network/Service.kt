package com.ardritkrasniqi.moti.network

import com.ardritkrasniqi.moti.BuildConfig
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

private const val BASE_URL = "https://api.openweathermap.org/data/2.5/"
private const val APPLICATION_KEY = BuildConfig.API_KEY

private const val GEODB_BASE_URL = "https://wft-geo-db.p.rapidapi.com/"
private const val GEODB_API_KEY = "acefc48d27msh4cb5f600d979efep1f1777jsnc0e4e747798d"
private const val GEODB_LIMIT = "10"


interface GeoDbService{
    @GET("v1/geo/cities")
    fun getCityByPrefix(
        @Query("namePrefix") cityPrefix: String,
        @Query("limit") limit: String = GEODB_LIMIT,
        @Header("x-rapidapi-key") key: String = GEODB_API_KEY
    ): Deferred<CityDataList>
}

// servis i retrofitit per te konsumu motin si forecast
interface WeatherService {
    @GET("forecast")
    fun getForecastWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("units") unit: String,
        @Query("appid") appId: String = APPLICATION_KEY
    ): Deferred<WeatherList>

    @GET("forecast")
    fun getForecastWeatherName(
        @Query("q") city: String,
        @Query("units") unit: String,
        @Query("appid") appId: String = APPLICATION_KEY
    ): Deferred<WeatherList>
}


// Pika hyrese per akses ne network
object MotiNetwork {
    // krijon ne instance te retrofitit
    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .baseUrl(BASE_URL)
        .build()

    val motiService: WeatherService by lazy {
        retrofit.create(WeatherService::class.java)
    }
}


object GeoDbNetwork{
    private val retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .baseUrl(GEODB_BASE_URL)
        .build()

    val geoDbService: GeoDbService by lazy {
        retrofit.create(GeoDbService::class.java)
    }
}
