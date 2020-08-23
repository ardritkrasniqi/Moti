package com.ardritkrasniqi.moti.ui.forecastFragment

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ardritkrasniqi.moti.R
import com.ardritkrasniqi.moti.UtilityClasses.HelperClass
import com.ardritkrasniqi.moti.domain.WeatherModel
import kotlinx.android.synthetic.main.weather_forecastlist_item.view.*


class ForecastAdapter(private val forecastList: MutableList<List<WeatherModel>?>) :
    RecyclerView.Adapter<ForecastAdapter.ForecastViewHolder>() {

    inner class ForecastViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val monthText = itemView.findViewById<TextView>(R.id.month_text)
        val weatherIcon = itemView.findViewById<ImageView>(R.id.weather_icon)
        val dayOfWeek = itemView.findViewById<TextView>(R.id.dayOfWeek)

        fun bind(item: List<WeatherModel>) {
            monthText.text = HelperClass.dateFormatterForForecastRecycler(item[0].dateText!!)
            dayOfWeek.text = HelperClass.getDayForForecastRecycler(item[0].dateText!!)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.weather_forecastlist_item, parent, false)
        return ForecastViewHolder(itemView)
    }

    override fun getItemCount() = forecastList.size

    override fun onBindViewHolder(holder: ForecastViewHolder, position: Int) {
        holder.itemView.isClickable = true
        val currentItem = forecastList[position]
        var rowIndex = 0
        holder.bind(currentItem!!)
        holder.itemView.setOnClickListener {
            rowIndex = position
            notifyDataSetChanged()
        }

        if(rowIndex == position){
            holder.itemView.month_text.setTextColor(Color.BLACK)
            holder.itemView.dayOfWeek.setTextColor(Color.BLACK)
            holder.weatherIcon.setColorFilter(Color.BLACK)
        } else {
            holder.itemView.month_text.setTextColor(Color.LTGRAY)
            holder.itemView.dayOfWeek.setTextColor(Color.LTGRAY)
            holder.weatherIcon.setColorFilter(Color.LTGRAY)
        }
    }

}