package com.ardritkrasniqi.moti.UtilityClasses

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ardritkrasniqi.moti.domain.WeatherForecastModel
import com.ardritkrasniqi.moti.domain.WeatherModel
import com.ardritkrasniqi.moti.network.CityData
import com.ardritkrasniqi.moti.network.CityDataList
import com.ardritkrasniqi.moti.ui.addNewCity.CityAdapter
import com.ardritkrasniqi.moti.ui.selectAddedCities.CitiesAdapter


@BindingAdapter("listData")
fun RecyclerView.bindRecyclerView(data: List<WeatherForecastModel>?) {
    data?.let {
        val adapter = this.adapter as CitiesAdapter
        adapter.submitList(it)
    }
}

@BindingAdapter("cityNameItem")
fun addName(textView: TextView, cityName: String?) {
    textView.text = cityName
}

@BindingAdapter("cityTemp")
fun addTemp(textView: TextView, cityTemp: String) {
    textView.text = "$cityTemp°"
}

@BindingAdapter("cityWeatherDescription")
fun addDescription(textView: TextView, cityTempDescription: String) {
    textView.text = cityTempDescription.capitalize()
}


@BindingAdapter("imageResource")
fun addImageResource(imageView: ImageView, imageResource: Int){
    imageView.setImageResource(imageResource)
}

@BindingAdapter("backgroundColorBinding")
fun addBackgroundResource(imageView: ImageView, colorResource: Int){
    imageView.setBackgroundResource(colorResource)
}


