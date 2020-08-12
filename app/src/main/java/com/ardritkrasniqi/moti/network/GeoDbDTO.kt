package com.ardritkrasniqi.moti.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class CityDataList(
    @Json(name = "data")
    val cityList: List<CityData>
)


data class CityData(
    val id:  Int = -1,
    val wikiDataId: String = "",
    val type: String = "",
    val city: String = "",
    val name: String = "",
    val country: String = "",
    val countryCode: String = "",
    val region: String = "",
    val regionCode: String = "",
    val latitude: String = "",
    val longitude: String = ""
)