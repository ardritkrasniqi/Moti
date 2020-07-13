package com.ardritkrasniqi.moti.network

import com.ardritkrasniqi.moti.BuildConfig
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://api.openweathermap.org/data/2.5/"
private const val APPLICATION_KEY = BuildConfig.API_KEY


// servis i retrofitit per te konsumu motin si forecast
interface WeatherService {
    @GET("forecast")
    fun getForecastWeather(
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
