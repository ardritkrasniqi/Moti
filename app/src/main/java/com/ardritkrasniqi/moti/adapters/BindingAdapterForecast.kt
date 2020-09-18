package com.ardritkrasniqi.moti.adapters

import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ardritkrasniqi.moti.R
import com.ardritkrasniqi.moti.domain.WeatherModel

@BindingAdapter("listDataForecast")
fun bindRecycler(recyclerView: RecyclerView, data: MutableList<List<WeatherModel>>?) {
    val adapter = recyclerView.adapter as ForecastAdapter
    adapter.submitList(data)
}


@BindingAdapter("forecastDate")
fun forecastDate(textView: TextView, forecastDateText: String) {
    textView.text = forecastDateText
}

@BindingAdapter("forecastDay")
fun forecastDay(textView: TextView, forecastDayText: String) {
    textView.text = forecastDayText
}

@BindingAdapter("forecastImage")
fun forecastImage(imageView: ImageView, weatherCondition: Int){
    imageView.setImageResource(
        when(weatherCondition){
            in 200..299 -> R.drawable.ic_cloudy
            in 300..399 -> R.drawable.ic_cloudy
            in 500..599 -> R.drawable.ic_rain
            in 600..699 -> R.drawable.ic_snow_cloud_simple
            in 700..799 -> R.drawable.ic_cloudy
            800 -> R.drawable.ic_sunny
            in 801..802 -> R.drawable.ic_005_cloudy_day_1
            in 803..900 -> R.drawable.ic_cloudy
            else -> R.drawable.ic_moon
        }
    )
}