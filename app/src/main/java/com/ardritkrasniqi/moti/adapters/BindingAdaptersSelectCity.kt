package com.ardritkrasniqi.moti.adapters

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.marginTop
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ardritkrasniqi.moti.R
import com.ardritkrasniqi.moti.domain.WeatherForecastModel


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
fun addImageResource(imageView: ImageView, imageResource: Int) {
    imageView.setImageResource(imageResource)
}

@BindingAdapter("backgroundColorBinding")
fun addBackgroundResource(imageView: ImageView, colorResource: Int) {
    imageView.setBackgroundResource(colorResource)
}

@BindingAdapter("onLongClick")
fun setOnLongClickListener(view: View, listener: Runnable) {
    view.setOnLongClickListener {
        listener.run();
        addImageResource(view as ImageView, R.drawable.selected_card_background)
        true }
}

