package com.ardritkrasniqi.moti.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class WeatherConverter {
    @TypeConverter
    fun listToJson(list: List<Weather>?): String = Gson().toJson(list)

    @TypeConverter
    fun jsonToList(value: String): List<Weather> {
        val listType = object : TypeToken<List<Weather>>() {}.type
        return Gson().fromJson(value, listType)
    }
}
