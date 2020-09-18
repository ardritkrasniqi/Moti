package com.ardritkrasniqi.moti.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ardritkrasniqi.moti.UtilityClasses.HelperClass
import com.ardritkrasniqi.moti.databinding.WeatherForecastlistItemBinding
import com.ardritkrasniqi.moti.domain.WeatherModel
import kotlinx.android.synthetic.main.weather_forecastlist_item.view.*


class ForecastAdapter(private val clickListener: OnClickListenerForecast) :
    ListAdapter<List<WeatherModel>, ForecastAdapter.ForecastViewHolder>(
        ForecastDiffCallback()
    ) {

    private var selectedPosition = 0
    private var currentForecast: MutableList<WeatherModel> = mutableListOf()


    inner class ForecastViewHolder(var binding: WeatherForecastlistItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            currentForecast: List<WeatherModel>,
            clickListener: OnClickListenerForecast
        ) {
            binding.imgResourceCode = currentForecast[0].weatherId
            binding.clickedItem = currentForecast.toString()
            binding.currentDate =
                HelperClass.dateFormatterForForecastRecycler(currentForecast[0].dateText!!)
            binding.currentDay =
                HelperClass.getDayForForecastRecycler(currentForecast[0].dateText!!)
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ForecastViewHolder(
            WeatherForecastlistItemBinding.inflate(
                layoutInflater,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ForecastViewHolder, position: Int) {
        holder.bind(getItem(position), clickListener)
        currentForecast = getItem(position) as MutableList<WeatherModel>

        if (selectedPosition == position) {
            holder.itemView.month_text.setTextColor(Color.BLACK)
            holder.itemView.day_text.setTextColor(Color.BLACK)
            holder.itemView.weather_icon.setColorFilter(Color.BLACK)
        } else {
            holder.itemView.month_text.setTextColor(Color.LTGRAY)
            holder.itemView.day_text.setTextColor(Color.LTGRAY)
            holder.itemView.weather_icon.setColorFilter(Color.LTGRAY)
        }



    }
}

class ForecastDiffCallback : DiffUtil.ItemCallback<List<WeatherModel>>() {
    override fun areItemsTheSame(
        oldItem: List<WeatherModel>,
        newItem: List<WeatherModel>
    ): Boolean {
        return oldItem[0].dateUnix == newItem[0].dateUnix
    }

    override fun areContentsTheSame(
        oldItem: List<WeatherModel>,
        newItem: List<WeatherModel>
    ): Boolean {
        return oldItem[0].dateUnix == newItem[0].dateUnix
    }


}

class OnClickListenerForecast(val forecastClickListener: (weatherForecast: String) -> Unit) {
    fun onClick(forecastWeather: String) = forecastClickListener(forecastWeather)
}
